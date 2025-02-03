package com.project.free.service;

import com.project.free.dto.like.LikesDeleteRequest;
import com.project.free.dto.like.LikesRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.LikesEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.LikesEntityRepository;
import com.project.free.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesEntityRepository likesEntityRepository;

    private final BoardEntityRepository boardEntityRepository;

    private final UserEntityRepository userEntityRepository;

    // 좋아요 누리기
    @Transactional
    public LikesResponse createLikes(LikesRequest likesRequest) {
        userEntityRepository.findById(likesRequest.getUserId()).orElseThrow(() -> new BaseException(ErrorResult.USER_NOT_FOUND));
        BoardEntity boardEntity = boardEntityRepository.findById(likesRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
        likesEntityRepository.findByUserIdAndBoardId(likesRequest.getUserId(), likesRequest.getBoardId())
                .ifPresent(likesEntity -> {throw new BaseException(ErrorResult.LIKES_DUPLICATE);});

        LikesEntity likesEntity = LikesEntity.builder()
                .boardId(likesRequest.getBoardId())
                .userId(likesRequest.getUserId())
                .isDeleted(false)
                .build();

        LikesEntity saved = likesEntityRepository.save(likesEntity);

        if (boardEntity.getLikes() == null) {
            boardEntity = BoardEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .writer(boardEntity.getWriter())
                    .views(boardEntity.getViews())
                    .comments(boardEntity.getComments())
                    .likes(new ArrayList<>())
                    .build();
        }

        boardEntity.getLikes().add(saved);
        boardEntityRepository.save(boardEntity);

        return LikesResponse.builder()
                .likesId(saved.getLikesId())
                .boardId(boardEntity.getBoardId())
                .userId(saved.getUserId())
                .build();
    }

    // 좋아요 취소
    public void deleteLikes(LikesDeleteRequest likesDeleteRequest) {
        userEntityRepository.findById(likesDeleteRequest.getUserId()).orElseThrow(() -> new BaseException(ErrorResult.USER_NOT_FOUND));
        BoardEntity boardEntity = boardEntityRepository.findById(likesDeleteRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
        LikesEntity likesEntity = likesEntityRepository.findByUserIdAndBoardId(likesDeleteRequest.getUserId(), likesDeleteRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.LIKES_NOT_FOUND));

        if (boardEntity.getLikes() == null) {
            boardEntity = BoardEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .writer(boardEntity.getWriter())
                    .views(boardEntity.getViews())
                    .comments(boardEntity.getComments())
                    .likes(new ArrayList<>())
                    .build();
        }

        boardEntity.getLikes().remove(likesEntity);
        likesEntity.setIsDeleted(true);
        likesEntity.setDeletedAt(LocalDateTime.now());

        boardEntityRepository.save(boardEntity);
        likesEntityRepository.save(likesEntity);
    }
}
