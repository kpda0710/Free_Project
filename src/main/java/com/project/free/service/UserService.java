package com.project.free.service;

import com.project.free.dto.shopping.ShoppingResponse;
import com.project.free.dto.user.*;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.UserEntity;
import com.project.free.entity.UserStatus;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.SellerEntityRepository;
import com.project.free.repository.UserEntityRepository;
import com.project.free.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final BoardService boardService;
    private final BoardEntityRepository boardEntityRepository;
    private final CommentService commentService;
    private final SellerService sellerService;
    private final SellerEntityRepository sellerEntityRepository;

    // 유저 가입
    @Transactional
    public UserResponse signupUser(UserRequest request) {
        UserEntity userEntity = UserEntity.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .status(UserStatus.USER)
                .isDeleted(false)
                .build();

        UserEntity saved = userEntityRepository.save(userEntity);

        return UserResponse.builder()
                .userId(saved.getUserId())
                .name(saved.getName())
                .password(saved.getPassword())
                .email(saved.getEmail())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // 유저 로그인
    @Transactional(readOnly = true)
    public String loginUser(UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();
        UserEntity userEntity = userEntityRepository.findByEmail(email).orElseThrow(() -> new BaseException(ResponseCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BaseException(ResponseCode.USER_NOT_PASSWORD);
        }

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .build();

        return jwtUtil.createAccessToken(userInfoDto);
    }

    // 유저 정보 가져오기
    @Transactional(readOnly = true)
    public UserResponse getUser(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        UserEntity userEntity = getUserEntity(principal.getUserInfoDto().getUserId());

        return UserResponse.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .status(userEntity.getStatus())
                .shoppingId(userEntity.getShoppingId())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    // 유저 정보 업데이트
    @Transactional
    public UserResponse updateUser(UserRequest request, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        UserEntity userEntity = getUserEntity(principal.getUserInfoDto().getUserId());

        UserEntity updatedUserEntity = UserEntity.builder()
                .userId(userEntity.getUserId())
                .name(request.getName())
                .password(request.getPassword())
                .email(request.getEmail())
                .status(userEntity.getStatus())
                .shoppingId(userEntity.getShoppingId())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isDeleted(userEntity.getIsDeleted())
                .build();

        UserEntity saved = userEntityRepository.save(updatedUserEntity);

        // 게시글 이름 업데이트
        boardService.updateBoardUserName(request, authentication);

        // 댓글 이름 업데이트
        commentService.updateCommentUserName(request, authentication);

        // 판매자 이름 및 이메일 업데이트
        sellerService.updateSellerUserNameAndUserEmail(request, authentication);

        return UserResponse.builder()
                .userId(saved.getUserId())
                .name(saved.getName())
                .password(saved.getPassword())
                .email(saved.getEmail())
                .status(saved.getStatus())
                .shoppingId(saved.getShoppingId())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // 유저 정보 삭제
    @Transactional
    public void deleteUser(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        UserEntity userEntity = getUserEntity(principal.getUserInfoDto().getUserId());

        List<BoardEntity> boardEntityList = boardEntityRepository.findByUserId(userEntity.getUserId());
        if (!boardEntityList.isEmpty()) {
            for (BoardEntity boardEntity : boardEntityList) {
                boardService.deleteBoard(boardEntity.getBoardId(), authentication);
            }
        }
        sellerService.deleteSeller(authentication);
        userEntity.deleteSetting();
        userEntityRepository.save(userEntity);
    }

    // 유저 정보 모두 가져오기 - 어드민 전용
    @Transactional(readOnly = true)
    public PageImpl<UserResponse> getUserAll(int pageNumber, Authentication authentication) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(sorts));
        Page<UserEntity> userEntities = userEntityRepository.findAll(pageable);

        List<UserResponse> userResponseList = userEntities.stream().map(userEntity ->
                UserResponse.builder()
                        .userId(userEntity.getUserId())
                        .name(userEntity.getName())
                        .password(userEntity.getPassword())
                        .email(userEntity.getEmail())
                        .status(userEntity.getStatus())
                        .shoppingId(userEntity.getShoppingId())
                        .createdAt(userEntity.getCreatedAt())
                        .updatedAt(userEntity.getUpdatedAt())
                        .build()).collect(Collectors.toList());

        return new PageImpl<>(userResponseList, pageable, userEntities.getTotalElements());
    }

    // 유저아이디로 유저 삭제하기 - 어드민 전용
    public void deleteUserById(Long userId, Authentication authentication) {
        UserEntity userEntity = getUserEntity(userId);
        userEntity.deleteSetting();
        userEntityRepository.save(userEntity);
    }

    // UserEntity 가져오기
    private UserEntity getUserEntity(Long userId) {
        return userEntityRepository.findById(userId).orElseThrow(() -> new BaseException(ResponseCode.USER_NOT_FOUND));
    }
}
