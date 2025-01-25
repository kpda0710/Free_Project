package com.project.free.service;

import com.project.free.dto.board.BoardDetailResponse;
import com.project.free.dto.board.BoardRequest;
import com.project.free.dto.board.BoardResponse;
import com.project.free.dto.board.BoardUpdateRequest;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.like.LikesResponse;
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

    // 게시글 생성
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

    // 게시글 전체 검색
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

    // 제목으로 게시글 검색
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

    // 게시글 자세히 보기
    public BoardDetailResponse getBoardByID(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        return BoardDetailResponse.builder()
                .boardId(boardEntity.getBoardId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .comments(boardEntity.getComments().stream().map(commentEntity ->
                        CommentResponse.builder()
                                .commentId(commentEntity.getCommentId())
                                .boardId(commentEntity.getBoardId())
                                .comment(commentEntity.getComment())
                                .writer(commentEntity.getWriter())
                                .createdAt(commentEntity.getCreatedAt())
                                .updatedAt(commentEntity.getUpdatedAt())
                                .build()).collect(Collectors.toList()))
                .likes(boardEntity.getLikes().stream().map(likesEntity ->
                        LikesResponse.builder()
                                .likesId(likesEntity.getLikesId())
                                .boardId(likesEntity.getBoardId())
                                .userId(likesEntity.getUserId())
                                .build()).collect(Collectors.toList()))
                .createdAt(boardEntity.getCreatedAt())
                .updatedAt(boardEntity.getUpdatedAt())
                .build();
    }

    // 좋아요 카운트
    public Integer getCountLikes(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);
        return boardEntity.getLikes().size();
    }

    // 게시글 수정
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

    // 게시글 삭제
    public void deleteBoard(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        boardEntity.setIsDeleted(true);
        boardEntity.setDeletedAt(LocalDateTime.now());

        boardEntityRepository.save(boardEntity);
    }

    // BoardEntity 가져오기
    private BoardEntity getBoardEntityByID(Long boardId) {
        return boardEntityRepository.findById(boardId).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
    }
}
