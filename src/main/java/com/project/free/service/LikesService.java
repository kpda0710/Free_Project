package com.project.free.service;

import com.project.free.dto.like.LikesRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.LikesEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.LikesEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesEntityRepository likesEntityRepository;

    private final BoardEntityRepository boardEntityRepository;

    // 좋아요 누리기
    public LikesResponse createLikes(LikesRequest likesRequest) {
        BoardEntity boardEntity = boardEntityRepository.findById(likesRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
        likesEntityRepository.findByUserIdAndBoardId(likesRequest.getUserId(), likesRequest.getBoardId())
                .ifPresent(likesEntity -> {throw new BaseException(ErrorResult.LIKES_DUPLICATE);});

        LikesEntity likesEntity = LikesEntity.builder()
                .boardId(likesRequest.getBoardId())
                .userId(likesRequest.getUserId())
                .build();

        LikesEntity saved = likesEntityRepository.save(likesEntity);

        if (boardEntity.getLikes() == null) {
            boardEntity.setLikes(new ArrayList<>());
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
    public void deleteLikes(Long likesId) {
        LikesEntity likesEntity = likesEntityRepository.findById(likesId).orElseThrow(() -> new BaseException(ErrorResult.LIKES_NOT_FOUND));
        BoardEntity boardEntity = boardEntityRepository.findById(likesEntity.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));

        if (boardEntity.getLikes() == null) {
            boardEntity.setLikes(new ArrayList<>());
        }

        boardEntity.getLikes().remove(likesEntity);
        likesEntity.setIsDeleted(true);
        likesEntity.setDeletedAt(LocalDateTime.now());

        boardEntityRepository.save(boardEntity);
        likesEntityRepository.save(likesEntity);
    }
}
