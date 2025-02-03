package com.project.free.service;

import com.project.free.dto.comment.CommentDeleteRequest;
import com.project.free.dto.comment.CommentRequest;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.comment.CommentUpdateRequest;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.CommentEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.CommentEntityRepository;
import com.project.free.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentEntityRepository commentEntityRepository;
    private final BoardEntityRepository boardEntityRepository;
    private final UserEntityRepository userEntityRepository;

    // 댓글 생성
    public CommentResponse createComment(CommentRequest commentRequest) {
        userEntityRepository.findById(commentRequest.getUserId()).orElseThrow(() -> new BaseException(ErrorResult.USER_NOT_FOUND));

        BoardEntity boardEntity = boardEntityRepository.findById(commentRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(commentRequest.getUserId())
                .boardId(commentRequest.getBoardId())
                .comment(commentRequest.getComment())
                .writer(commentRequest.getWriter())
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
                .comment(commentEntity.getComment())
                .writer(commentEntity.getWriter())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }

    // 댓글 수정
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        CommentEntity commentEntity = getCommentEntity(commentId);

        userEntityRepository.findById(commentUpdateRequest.getUserId()).orElseThrow(() -> new BaseException(ErrorResult.USER_NOT_FOUND));

        CommentEntity updatedCommentEntity = CommentEntity.builder()
                .commentId(commentEntity.getCommentId())
                .boardId(commentEntity.getBoardId())
                .userId(commentEntity.getUserId())
                .comment(commentUpdateRequest.getComment())
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
                .comment(saved.getComment())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // 댓글 삭제
    public void deleteComment(CommentDeleteRequest commentDeleteRequest) {
        userEntityRepository.findById(commentDeleteRequest.getUserId()).orElseThrow(() -> new BaseException(ErrorResult.USER_NOT_FOUND));
        BoardEntity boardEntity = boardEntityRepository.findById(commentDeleteRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));

        CommentEntity commentEntity = getCommentEntity(commentDeleteRequest.getCommentId());

        commentEntity.setIsDeleted(true);
        commentEntity.setDeletedAt(LocalDateTime.now());

        List<CommentEntity> boardEntityComments = boardEntity.getComments();
        boardEntityComments.remove(commentEntity);

        commentEntityRepository.save(commentEntity);
        boardEntityRepository.save(boardEntity);
    }

    // CommentEntity 가져오기
    private CommentEntity getCommentEntity(Long commentId) {
        return commentEntityRepository.findById(commentId).orElseThrow(() -> new BaseException(ErrorResult.COMMENT_NOT_FOUND));
    }
}
