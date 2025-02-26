package com.project.free.service;

import com.project.free.dto.like.LikesResponse;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.ItemEntity;
import com.project.free.entity.LikesEntity;
import com.project.free.entity.LikesStatus;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.ItemEntityRepository;
import com.project.free.repository.LikesEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesEntityRepository likesEntityRepository;
    private final BoardEntityRepository boardEntityRepository;
    private final ItemEntityRepository itemEntityRepository;

    @Transactional
    // 좋아요 누르기(게시글)
    public LikesResponse createLikesByBoard(Long boardId, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        BoardEntity boardEntity = boardEntityRepository.findById(boardId).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));
        likesEntityRepository.findByUserIdAndTargetIdAndStatus(userInfoDto.getUserId(), boardId, LikesStatus.BOARD)
                .ifPresent(likesEntity -> {throw new BaseException(ResponseCode.LIKES_DUPLICATE);});

        LikesEntity likesEntity = LikesEntity.builder()
                .targetId(boardId)
                .userId(userInfoDto.getUserId())
                .status(LikesStatus.BOARD)
                .isDeleted(false)
                .build();

        LikesEntity saved = likesEntityRepository.save(likesEntity);

        boardEntity.getLikes().add(saved);
        boardEntityRepository.save(boardEntity);

        return LikesResponse.builder()
                .likesId(saved.getLikesId())
                .targetId(boardEntity.getBoardId())
                .userId(saved.getUserId())
                .status(saved.getStatus())
                .build();
    }

    @Transactional
    // 좋아요 누르기(상품)
    public LikesResponse createLikesByItem(Long itemId, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        ItemEntity itemEntity = itemEntityRepository.findById(itemId).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));
        likesEntityRepository.findByUserIdAndTargetIdAndStatus(userInfoDto.getUserId(), itemId, LikesStatus.ITEM)
                .ifPresent(likesEntity -> {throw new BaseException(ResponseCode.LIKES_DUPLICATE);});

        LikesEntity likesEntity = LikesEntity.builder()
                .targetId(itemId)
                .userId(userInfoDto.getUserId())
                .isDeleted(false)
                .status(LikesStatus.ITEM)
                .build();

        LikesEntity saved = likesEntityRepository.save(likesEntity);

        itemEntity.getLikes().add(saved);
        itemEntityRepository.save(itemEntity);

        return LikesResponse.builder()
                .likesId(saved.getLikesId())
                .targetId(saved.getTargetId())
                .userId(saved.getUserId())
                .status(saved.getStatus())
                .build();
    }

    @Transactional
    // 좋아요 취소(게시글)
    public void deleteLikesByBoard(Long targetId, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        BoardEntity boardEntity = boardEntityRepository.findById(targetId).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));
        LikesEntity likesEntity = likesEntityRepository.findByUserIdAndTargetIdAndStatus(userInfoDto.getUserId(), targetId, LikesStatus.BOARD).orElseThrow(() -> new BaseException(ResponseCode.LIKES_NOT_FOUND));

        boardEntity.getLikes().remove(likesEntity);
        likesEntity.deleteSetting();

        boardEntityRepository.save(boardEntity);
        likesEntityRepository.save(likesEntity);
    }

    @Transactional
    // 좋아요 취소(상품)
    public void deleteLikesByItem(Long targetId, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        ItemEntity itemEntity = itemEntityRepository.findById(targetId).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));
        LikesEntity likesEntity = likesEntityRepository.findByUserIdAndTargetIdAndStatus(userInfoDto.getUserId(), targetId, LikesStatus.ITEM).orElseThrow(() -> new BaseException(ResponseCode.LIKES_NOT_FOUND));

        itemEntity.getLikes().remove(likesEntity);
        likesEntity.deleteSetting();

        itemEntityRepository.save(itemEntity);
        likesEntityRepository.save(likesEntity);
    }

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }
}
