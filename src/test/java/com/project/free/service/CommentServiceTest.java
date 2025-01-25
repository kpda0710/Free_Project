package com.project.free.service;

import com.project.free.dto.comment.CommentRequest;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.comment.CommentUpdateRequest;
import com.project.free.entity.CommentEntity;
import com.project.free.repository.CommentEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentEntityRepository commentEntityRepository;

    @Test
    public void createComment() throws Exception {
        CommentEntity commentEntity = CommentEntity.builder()
                .commentId(1L)
                .boardId(1L)
                .comment("comment")
                .writer("writer")
                .build();

        CommentRequest commentRequest = CommentRequest.builder()
                .boardId(1L)
                .comment("comment")
                .writer("writer")
                .build();

        when(commentEntityRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);

        CommentResponse result = commentService.createComment(commentRequest);

        assertThat(result.getBoardId()).isEqualTo(commentEntity.getBoardId());
        assertThat(result.getComment()).isEqualTo(commentEntity.getComment());
        assertThat(result.getWriter()).isEqualTo(commentEntity.getWriter());
    }

    @Test
    public void updateComment() throws Exception {
        CommentEntity commentEntity = CommentEntity.builder()
                .commentId(1L)
                .boardId(1L)
                .comment("comment")
                .writer("writer")
                .build();

        CommentUpdateRequest commentRequest = CommentUpdateRequest.builder()
                .comment("comment@@")
                .build();

        when(commentEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(commentEntity));
        when(commentEntityRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);

        CommentResponse result = commentService.updateComment(1L, commentRequest);

        assertThat(result.getComment()).isEqualTo(commentRequest.getComment());
    }

    @Test
    public void deleteComment() throws Exception {
        CommentEntity commentEntity = CommentEntity.builder()
                .commentId(1L)
                .boardId(1L)
                .comment("comment")
                .writer("writer")
                .build();
        when(commentEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(commentEntity));
        when(commentEntityRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);

        commentService.deleteComment(1L);

        assertThat(commentEntity.getIsDeleted()).isTrue();
    }
}