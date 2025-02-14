package com.project.free.service;

import com.project.free.dto.comment.CommentReplyResponse;
import com.project.free.dto.comment.CommentRequest;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.comment.CommentUpdateRequest;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.dto.user.UserRequest;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.CommentEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.CommentEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentEntityRepository commentEntityRepository;
    private final BoardEntityRepository boardEntityRepository;

    @Transactional
    // 댓글 생성
    public CommentResponse createComment(CommentRequest commentRequest, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        BoardEntity boardEntity = boardEntityRepository.findById(commentRequest.getBoardId()).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(userInfoDto.getUserId())
                .boardId(commentRequest.getBoardId())
                .content(commentRequest.getContent())
                .writer(userInfoDto.getName())
                .isDeleted(false)
                .build();

        List<CommentEntity> boardEntityComments = boardEntity.getComments();
        boardEntityComments.add(commentEntity);

        commentEntityRepository.save(commentEntity);
        boardEntityRepository.save(boardEntity);

        return CommentResponse.builder()
                .commentId(commentEntity.getCommentId())
                .userId(commentEntity.getUserId())
                .boardId(commentEntity.getBoardId())
                .content(commentEntity.getContent())
                .writer(commentEntity.getWriter())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }

    @Transactional
    // 댓글 수정
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        CommentEntity commentEntity = getCommentEntity(commentId);

        CommentEntity updatedCommentEntity = CommentEntity.builder()
                .commentId(commentEntity.getCommentId())
                .boardId(commentEntity.getBoardId())
                .userId(commentEntity.getUserId())
                .content(commentUpdateRequest.getContent())
                .writer(commentEntity.getWriter())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isDeleted(commentEntity.getIsDeleted())
                .build();

        CommentEntity saved = commentEntityRepository.save(updatedCommentEntity);

        return CommentResponse.builder()
                .commentId(saved.getCommentId())
                .boardId(saved.getBoardId())
                .userId(saved.getUserId())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional
    // 댓글 삭제
    public void deleteComment(Long commentId) {
        CommentEntity commentEntity = getCommentEntity(commentId);
        BoardEntity boardEntity = boardEntityRepository.findById(commentEntity.getBoardId()).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));

        commentEntity.deleteSetting();

        List<CommentEntity> boardEntityComments = boardEntity.getComments();
        boardEntityComments.remove(commentEntity);

        commentEntityRepository.save(commentEntity);
        boardEntityRepository.save(boardEntity);
    }

    @Transactional
    public CommentReplyResponse createReply(Long commentId, CommentRequest commentRequest, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        CommentEntity reply = commentEntityRepository.findById(commentId).orElseThrow(() -> new BaseException(ResponseCode.COMMENT_NOT_FOUND));

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(userInfoDto.getUserId())
                .boardId(commentRequest.getBoardId())
                .content(commentRequest.getContent())
                .writer(userInfoDto.getName())
                .isDeleted(false)
                .build();

        CommentEntity saved = commentEntityRepository.save(commentEntity);

        reply.getReply().add(commentEntity);
        commentEntityRepository.save(reply);

        return CommentReplyResponse.builder()
                .commentId(saved.getCommentId())
                .userId(saved.getUserId())
                .boardId(saved.getBoardId())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // CommentEntity 가져오기
    private CommentEntity getCommentEntity(Long commentId) {
        return commentEntityRepository.findById(commentId).orElseThrow(() -> new BaseException(ResponseCode.COMMENT_NOT_FOUND));
    }

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }

    // 유저 이름 변경으로 댓글 작성자 이름 변경
    public void updateCommentUserName(UserRequest request, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        List<CommentEntity> commentEntities = commentEntityRepository.findByUserId(userInfoDto.getUserId());

        for (CommentEntity commentEntity : commentEntities) {
            commentEntity.updateWriter(request.getName());
        }
    }
}
