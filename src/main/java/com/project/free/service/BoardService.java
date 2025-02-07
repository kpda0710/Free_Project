package com.project.free.service;

import com.project.free.dto.board.*;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.like.LikesResponse;
import com.project.free.entity.BoardEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardEntityRepository boardEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    // 게시글 생성
    public BoardResponse createBoard(BoardRequest boardRequest) {
        userEntityRepository.findById(boardRequest.getUserId()).orElseThrow(() -> new BaseException(ErrorResult.USER_NOT_FOUND));

        BoardEntity boardEntity = BoardEntity.builder()
                .userId(boardRequest.getUserId())
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(boardRequest.getWriter())
                .views(0L)
                .isDeleted(false)
                .build();

        BoardEntity saved = boardEntityRepository.save(boardEntity);

        return BoardResponse.builder()
                .boardId(saved.getBoardId())
                .userId(saved.getUserId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    // 게시글 전체 검색
    public List<BoardResponse> getAllBoards() {
        List<BoardEntity> boardEntityList = boardEntityRepository.findAll();

        return boardEntityList.stream().map(boardEntity ->
                BoardResponse.builder()
                        .boardId(boardEntity.getBoardId())
                        .userId(boardEntity.getUserId())
                        .title(boardEntity.getTitle())
                        .content(boardEntity.getContent())
                        .writer(boardEntity.getWriter())
                        .createdAt(boardEntity.getCreatedAt())
                        .updatedAt(boardEntity.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    // 제목으로 게시글 검색
    public List<BoardResponse> getBoardsByTitle(String title) {
        List<BoardEntity> boardEntityList = boardEntityRepository.findByTitle(title);

        return boardEntityList.stream().map(boardEntity ->
                        BoardResponse.builder()
                                .boardId(boardEntity.getBoardId())
                                .userId(boardEntity.getUserId())
                                .title(boardEntity.getTitle())
                                .content(boardEntity.getContent())
                                .writer(boardEntity.getWriter())
                                .createdAt(boardEntity.getCreatedAt())
                                .updatedAt(boardEntity.getUpdatedAt())
                                .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    // 게시글 자세히 보기
    public BoardDetailResponse getBoardByID(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        boardEntity = BoardEntity.builder()
                .boardId(boardEntity.getBoardId())
                .userId(boardEntity.getUserId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .views(boardEntity.getViews() + 1)
                .comments(boardEntity.getComments())
                .likes(boardEntity.getLikes())
                .createdAt(boardEntity.getCreatedAt())
                .updatedAt(boardEntity.getUpdatedAt())
                .isDeleted(boardEntity.getIsDeleted())
                .build();

        BoardEntity saved = boardEntityRepository.save(boardEntity);

        return BoardDetailResponse.builder()
                .boardId(saved.getBoardId())
                .userId(saved.getUserId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .views(saved.getViews())
                .comments(saved.getComments().stream().map(commentEntity ->
                        CommentResponse.builder()
                                .commentId(commentEntity.getCommentId())
                                .boardId(commentEntity.getBoardId())
                                .comment(commentEntity.getComment())
                                .writer(commentEntity.getWriter())
                                .createdAt(commentEntity.getCreatedAt())
                                .updatedAt(commentEntity.getUpdatedAt())
                                .build()).collect(Collectors.toList()))
                .likes(saved.getLikes().stream().map(likesEntity ->
                        LikesResponse.builder()
                                .likesId(likesEntity.getLikesId())
                                .boardId(likesEntity.getBoardId())
                                .userId(likesEntity.getUserId())
                                .build()).collect(Collectors.toList()))
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    // 좋아요 카운트
    public Integer getCountLikes(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);
        return boardEntity.getLikes().size();
    }

    @Transactional
    // 게시글 수정
    public BoardResponse updateBoard(Long boardId, BoardUpdateRequest boardRequest) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        if (!boardEntity.getUserId().equals(boardRequest.getUserId())) {
            throw new BaseException(ErrorResult.BOARD_USERID_NOT_MATCH);
        }

        BoardEntity updatedBoardEntity = BoardEntity.builder()
                .boardId(boardEntity.getBoardId())
                .userId(boardRequest.getUserId())
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(boardEntity.getWriter())
                .views(boardEntity.getViews())
                .comments(boardEntity.getComments())
                .likes(boardEntity.getLikes())
                .createdAt(boardEntity.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isDeleted(boardEntity.getIsDeleted())
                .build();

        BoardEntity saved = boardEntityRepository.save(updatedBoardEntity);

        return BoardResponse.builder()
                .boardId(saved.getBoardId())
                .userId(saved.getUserId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional
    // 게시글 삭제
    public void deleteBoard(Long boardId, BoardDeleteRequest boardDeleteRequest) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        if (!boardEntity.getUserId().equals(boardDeleteRequest.getUserId())) {
            throw new BaseException(ErrorResult.BOARD_USERID_NOT_MATCH);
        }

        boardEntity.setIsDeleted(true);
        boardEntity.setDeletedAt(LocalDateTime.now());

        boardEntityRepository.save(boardEntity);
    }

    // BoardEntity 가져오기
    private BoardEntity getBoardEntityByID(Long boardId) {
        return boardEntityRepository.findById(boardId).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
    }
}
