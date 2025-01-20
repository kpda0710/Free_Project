package com.project.free.service;

import com.project.free.dto.board.BoardRequest;
import com.project.free.dto.board.BoardResponse;
import com.project.free.dto.board.BoardUpdateRequest;
import com.project.free.entity.BoardEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.BoardEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardEntityRepository boardEntityRepository;

    public BoardResponse createBoard(BoardRequest boardRequest) {
        BoardEntity boardEntity = BoardEntity.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(boardRequest.getWriter())
                .build();

        BoardEntity saved = boardEntityRepository.save(boardEntity);

        return BoardResponse.builder()
                .boardId(saved.getBoardId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public List<BoardResponse> getAllBoards() {
        List<BoardEntity> boardEntityList = boardEntityRepository.findAll();

        return boardEntityList.stream().map(boardEntity ->
                BoardResponse.builder()
                        .boardId(boardEntity.getBoardId())
                        .title(boardEntity.getTitle())
                        .content(boardEntity.getContent())
                        .writer(boardEntity.getWriter())
                        .createdAt(boardEntity.getCreatedAt())
                        .updatedAt(boardEntity.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<BoardResponse> getBoardsByTitle(String title) {
        List<BoardEntity> boardEntityList = boardEntityRepository.findByTitle(title);

        return boardEntityList.stream().map(boardEntity ->
                        BoardResponse.builder()
                                .boardId(boardEntity.getBoardId())
                                .title(boardEntity.getTitle())
                                .content(boardEntity.getContent())
                                .writer(boardEntity.getWriter())
                                .createdAt(boardEntity.getCreatedAt())
                                .updatedAt(boardEntity.getUpdatedAt())
                                .build())
                .collect(Collectors.toList());
    }

    public BoardResponse getBoardByID(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        return BoardResponse.builder()
                .boardId(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .createdAt(boardEntity.getCreatedAt())
                .updatedAt(boardEntity.getUpdatedAt())
                .build();
    }

    public BoardResponse updateBoard(Long boardId, BoardUpdateRequest boardRequest) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        boardEntity.setTitle(boardRequest.getTitle());
        boardEntity.setContent(boardRequest.getContent());

        BoardEntity saved = boardEntityRepository.save(boardEntity);

        return BoardResponse.builder()
                .boardId(saved.getBoardId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public void deleteBoard(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        boardEntity.setIsDeleted(true);
        boardEntity.setDeletedAt(LocalDateTime.now());

        boardEntityRepository.save(boardEntity);
    }

    private BoardEntity getBoardEntityByID(Long boardId) {
        return boardEntityRepository.findById(boardId).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
    }
}
