package com.project.free.service;

import com.project.free.dto.like.LikesRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.LikesEntity;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.LikesEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @InjectMocks
    private LikesService likesService;

    @Mock
    private LikesEntityRepository likesEntityRepository;

    @Mock
    private BoardEntityRepository boardEntityRepository;

    @Test
    public void createLikes() throws Exception {
        BoardEntity boardEntity = BoardEntity.builder()
                .boardId(1L)
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        LikesEntity likesEntity = LikesEntity.builder()
                .likesId(1L)
                .boardId(1L)
                .userId(1L)
                .build();

        LikesRequest likesRequest = LikesRequest.builder()
                .boardId(1L)
                .userId(1L)
                .build();

        when(boardEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(boardEntity));
        when(likesEntityRepository.findByUserIdAndBoardId(1L, 1L)).thenReturn(Optional.empty());
        when(likesEntityRepository.save(any(LikesEntity.class))).thenReturn(likesEntity);
        when(boardEntityRepository.save(any(BoardEntity.class))).thenReturn(boardEntity);

        LikesResponse result = likesService.createLikes(likesRequest);

        assertThat(result.getBoardId()).isEqualTo(boardEntity.getBoardId());
        assertThat(result.getUserId()).isEqualTo(likesEntity.getUserId());
    }

    @Test
    public void deleteLikes() throws Exception {
        BoardEntity boardEntity = BoardEntity.builder()
                .boardId(1L)
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        LikesEntity likesEntity = LikesEntity.builder()
                .likesId(1L)
                .boardId(1L)
                .userId(1L)
                .build();

        when(likesEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(likesEntity));
        when(boardEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(boardEntity));
        when(likesEntityRepository.save(any(LikesEntity.class))).thenReturn(likesEntity);
        when(boardEntityRepository.save(any(BoardEntity.class))).thenReturn(boardEntity);

        likesService.deleteLikes(1L);

        assertThat(likesEntity.getIsDeleted()).isTrue();
    }
}