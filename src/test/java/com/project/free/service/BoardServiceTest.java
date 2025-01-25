package com.project.free.service;

import com.project.free.dto.board.BoardDetailResponse;
import com.project.free.dto.board.BoardRequest;
import com.project.free.dto.board.BoardResponse;
import com.project.free.dto.board.BoardUpdateRequest;
import com.project.free.entity.BoardEntity;
import com.project.free.repository.BoardEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardEntityRepository boardEntityRepository;

    @Test
    public void createBoard() throws Exception {
        BoardEntity boardEntity = getBoardEntity();

        BoardRequest boardRequest = BoardRequest.builder()
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        when(boardEntityRepository.save(any(BoardEntity.class))).thenReturn(boardEntity);

        BoardResponse result = boardService.createBoard(boardRequest);

        assertThat(result.getTitle()).isEqualTo(boardEntity.getTitle());
        assertThat(result.getContent()).isEqualTo(boardEntity.getContent());
        assertThat(result.getWriter()).isEqualTo(boardEntity.getWriter());
    }

    @Test
    public void getAllBoards() throws Exception {
        BoardEntity boardEntity1 = BoardEntity.builder()
                .boardId(1L)
                .title("title1")
                .content("content1")
                .writer("writer1")
                .build();

        BoardEntity boardEntity2 = BoardEntity.builder()
                .boardId(2L)
                .title("title2")
                .content("content2")
                .writer("writer2")
                .build();

        when(boardEntityRepository.findAll()).thenReturn(List.of(boardEntity1, boardEntity2));

        List<BoardResponse> result = boardService.getAllBoards();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTitle()).isEqualTo(boardEntity1.getTitle());
        assertThat(result.get(1).getTitle()).isEqualTo(boardEntity2.getTitle());
    }

    @Test
    public void getBoardsByTitle() throws Exception {
        BoardEntity boardEntity1 = BoardEntity.builder()
                .boardId(1L)
                .title("title1")
                .content("content1")
                .writer("writer1")
                .build();

        BoardEntity boardEntity2 = BoardEntity.builder()
                .boardId(2L)
                .title("title2")
                .content("content2")
                .writer("writer2")
                .build();

        when(boardEntityRepository.findByTitle(boardEntity1.getTitle())).thenReturn(List.of(boardEntity1));

        List<BoardResponse> result = boardService.getBoardsByTitle(boardEntity1.getTitle());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo(boardEntity1.getTitle());
        assertThat(result.get(0).getContent()).isEqualTo(boardEntity1.getContent());
        assertThat(result.get(0).getWriter()).isEqualTo(boardEntity1.getWriter());
    }

    @Test
    public void getBoardByID() throws Exception {
        BoardEntity boardEntity = getBoardEntity();

        when(boardEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(boardEntity));

        BoardDetailResponse result = boardService.getBoardByID(1L);

        assertThat(result.getTitle()).isEqualTo(boardEntity.getTitle());
        assertThat(result.getContent()).isEqualTo(boardEntity.getContent());
        assertThat(result.getWriter()).isEqualTo(boardEntity.getWriter());
    }

    @Test
    public void updateBoard() throws Exception {
        BoardEntity boardEntity = getBoardEntity();

        BoardUpdateRequest boardUpdateRequest = BoardUpdateRequest.builder()
                .title("title1")
                .content("content1")
                .build();

        when(boardEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(boardEntity));
        when(boardEntityRepository.save(any(BoardEntity.class))).thenReturn(boardEntity);

        BoardResponse result = boardService.updateBoard(1L, boardUpdateRequest);

        assertThat(result.getTitle()).isEqualTo(boardUpdateRequest.getTitle());
        assertThat(result.getContent()).isEqualTo(boardUpdateRequest.getContent());
    }

    @Test
    public void deleteBoard() throws Exception {
        BoardEntity boardEntity = getBoardEntity();

        when(boardEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(boardEntity));
        when(boardEntityRepository.save(any(BoardEntity.class))).thenReturn(boardEntity);

        boardService.deleteBoard(1L);

        assertThat(boardEntity.getIsDeleted()).isTrue();
    }

    private static BoardEntity getBoardEntity() {
        return BoardEntity.builder()
                .boardId(1L)
                .title("title")
                .content("content")
                .writer("writer")
                .build();
    }
}