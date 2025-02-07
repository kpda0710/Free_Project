package com.project.free.service;

import com.project.free.dto.like.LikesDeleteRequest;
import com.project.free.dto.like.LikesRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.dto.user.UserRequest;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.CommentEntity;
import com.project.free.entity.LikesEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.LikesEntityRepository;
import com.project.free.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesEntityRepository likesEntityRepository;

    private final BoardEntityRepository boardEntityRepository;

    private final UserEntityRepository userEntityRepository;

    @Transactional
    // 좋아요 누르기
    public LikesResponse createLikes(LikesRequest likesRequest, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        BoardEntity boardEntity = boardEntityRepository.findById(likesRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
        likesEntityRepository.findByUserIdAndBoardId(userInfoDto.getUserId(), likesRequest.getBoardId())
                .ifPresent(likesEntity -> {throw new BaseException(ErrorResult.LIKES_DUPLICATE);});

        LikesEntity likesEntity = LikesEntity.builder()
                .boardId(likesRequest.getBoardId())
                .userId(userInfoDto.getUserId())
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

    @Transactional
    // 좋아요 취소
    public void deleteLikes(LikesDeleteRequest likesDeleteRequest, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        BoardEntity boardEntity = boardEntityRepository.findById(likesDeleteRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
        LikesEntity likesEntity = likesEntityRepository.findByUserIdAndBoardId(userInfoDto.getUserId(), likesDeleteRequest.getBoardId()).orElseThrow(() -> new BaseException(ErrorResult.LIKES_NOT_FOUND));

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

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }
}
