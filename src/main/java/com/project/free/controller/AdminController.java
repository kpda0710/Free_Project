package com.project.free.controller;

import com.project.free.dto.user.UserResponse;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.UserEntity;
import com.project.free.entity.UserStatus;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.UserEntityRepository;
import com.project.free.service.BoardService;
import com.project.free.service.CommentService;
import com.project.free.service.PhotoService;
import com.project.free.service.UserService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final PhotoService photoService;
    private final UserEntityRepository userEntityRepository;
    private final BoardEntityRepository boardEntityRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 관련 -----------------------------------------------------------------


    // 모든 유저 정보 가져오는 API
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/all")
    public CustomResponse<PageImpl<UserResponse>> getAllUsers(@RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        PageImpl<UserResponse> userResponses = userService.getUserAll(page, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, userResponses);
    }

    // 유저 아이디로 유저 정보 삭제하는 API
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{userId}")
    public CustomResponse<Void> deleteUserById(@PathVariable(name = "userId") Long userId, Authentication authentication) {
        userService.deleteUserById(userId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }

    // 게시판 관련 -----------------------------------------------------------------


    // 게시판 아이디로 게시판 삭제하는 API
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/boards/{boardId}")
    public CustomResponse<Void> deleteBoardById(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        boardService.deleteBoardById(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }

    // 댓글 관련


    // 댓글 or 답글 아이디로 댓글 or 답글 삭제하는 API
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/comments/{commentId}")
    public CustomResponse<Void> deleteCommentById(@PathVariable(name = "commentId") Long commentId, Authentication authentication) {
        commentService.deleteCommentById(commentId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }

    // 게시판 사진 관련


    // 게시판 이미지 삭제하는 API
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/images/{photoId}")
    public CustomResponse<String> deleteImage(@PathVariable(name = "photoId") Long photoId, Authentication authentication) {
        photoService.deleteImageById(photoId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }

    // 데스트 데이터 관련 -----------------------------------------------------------------


    // 더미 데이터 추가하는 API
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/init")
    public CustomResponse<Void> initData(Authentication authentication) {
        for (int i = 0; i < 5; i++) {
            UserEntity userEntity = UserEntity.builder()
                    .name("Jung" + i)
                    .password(passwordEncoder.encode("password" + i))
                    .email("jung" + i + "@gmail.com")
                    .status(UserStatus.USER)
                    .isDeleted(false)
                    .build();
            userEntityRepository.save(userEntity);
        }

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 10; i++) {
                BoardEntity boardEntity = BoardEntity.builder()
                        .userId(Long.valueOf(j))
                        .title("title" + i)
                        .content("content" + i)
                        .writer("Jung" + j)
                        .views(0L)
                        .isDeleted(false)
                        .build();
                boardEntityRepository.save(boardEntity);
            }
        }
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }
}
