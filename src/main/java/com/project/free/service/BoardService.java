package com.project.free.service;

import com.project.free.dto.board.*;
import com.project.free.dto.comment.CommentReplyResponse;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.image.photoResponse;
import com.project.free.dto.like.LikesResponse;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.dto.user.UserRequest;
import com.project.free.entity.BoardEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ErrorResult;
import com.project.free.repository.BoardEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardEntityRepository boardEntityRepository;

    @Transactional
    // 게시글 생성
    public BoardResponse createBoard(BoardRequest boardRequest, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        BoardEntity boardEntity = BoardEntity.builder()
                .userId(userInfoDto.getUserId())
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .writer(userInfoDto.getName())
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
    public Page<BoardResponse> getAllBoards(int pageNumber) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(sorts));
        Page<BoardEntity> boardEntities = boardEntityRepository.findAll(pageable);

        List<BoardResponse> boardResponses = boardEntities.stream().map(boardEntity ->
                BoardResponse.builder()
                        .boardId(boardEntity.getBoardId())
                        .userId(boardEntity.getUserId())
                        .title(boardEntity.getTitle())
                        .content(boardEntity.getContent())
                        .writer(boardEntity.getWriter())
                        .createdAt(boardEntity.getCreatedAt())
                        .updatedAt(boardEntity.getUpdatedAt())
                        .build()).collect(Collectors.toList());

        return new PageImpl<>(boardResponses, pageable, boardEntities.getTotalElements());
    }

    @Transactional(readOnly = true)
    // 제목으로 게시글 검색
    public Page<BoardResponse> getBoardsByTitle(String title, int pageNumber) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(sorts));
        Page<BoardEntity> boardEntityList = boardEntityRepository.findAllByTitleContaining(title, pageable);

        List<BoardResponse> boardResponses = boardEntityList.stream().map(boardEntity ->
                        BoardResponse.builder()
                                .boardId(boardEntity.getBoardId())
                                .userId(boardEntity.getUserId())
                                .title(boardEntity.getTitle())
                                .content(boardEntity.getContent())
                                .writer(boardEntity.getWriter())
                                .createdAt(boardEntity.getCreatedAt())
                                .updatedAt(boardEntity.getUpdatedAt())
                                .build()).collect(Collectors.toList());

        return new PageImpl<>(boardResponses, pageable, boardEntityList.getTotalElements());
    }

    @Transactional(readOnly = true)
    // 게시글 자세히 보기
    public BoardDetailResponse getBoardByID(Long boardId) {
        BoardEntity boardEntity = getBoardEntityByID(boardId);

        boardEntity.viewPlus();

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
                                .userId(commentEntity.getUserId())
                                .boardId(commentEntity.getBoardId())
                                .content(commentEntity.getContent())
                                .writer(commentEntity.getWriter())
                                .reply(commentEntity.getReply().stream().map(entity -> CommentReplyResponse
                                        .builder()
                                        .commentId(entity.getCommentId())
                                        .userId(entity.getUserId())
                                        .boardId(entity.getBoardId())
                                        .content(entity.getContent())
                                        .writer(entity.getWriter())
                                        .createdAt(entity.getCreatedAt())
                                        .updatedAt(entity.getUpdatedAt())
                                        .build())
                                        .collect(Collectors.toList()))
                                .createdAt(commentEntity.getCreatedAt())
                                .updatedAt(commentEntity.getUpdatedAt())
                                .build()).collect(Collectors.toList()))
                .likes(saved.getLikes().stream().map(likesEntity ->
                        LikesResponse.builder()
                                .likesId(likesEntity.getLikesId())
                                .boardId(likesEntity.getBoardId())
                                .userId(likesEntity.getUserId())
                                .build()).collect(Collectors.toList()))
                .photo(saved.getPhotos().stream().map(imageEntity ->
                        photoResponse.builder()
                                .photoId(imageEntity.getPhotoId())
                                .boardId(imageEntity.getBoardId())
                                .imagePath(imageEntity.getPhotoPath())
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
    public BoardResponse updateBoard(Long boardId, BoardUpdateRequest boardRequest, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        BoardEntity boardEntity = getBoardEntityByID(boardId);

        if (!boardEntity.getUserId().equals(userInfoDto.getUserId())) {
            throw new BaseException(ErrorResult.BOARD_USERID_NOT_MATCH);
        }

        BoardEntity updatedBoardEntity = BoardEntity.builder()
                .boardId(boardEntity.getBoardId())
                .userId(userInfoDto.getUserId())
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
    public void deleteBoard(Long boardId, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        BoardEntity boardEntity = getBoardEntityByID(boardId);

        if (!boardEntity.getUserId().equals(userInfoDto.getUserId())) {
            throw new BaseException(ErrorResult.BOARD_USERID_NOT_MATCH);
        }

        boardEntity.deleteSetting();

        boardEntityRepository.save(boardEntity);
    }

    // BoardEntity 가져오기
    private BoardEntity getBoardEntityByID(Long boardId) {
        return boardEntityRepository.findById(boardId).orElseThrow(() -> new BaseException(ErrorResult.BOARD_NOT_FOUND));
    }

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }

    // 유저 이름 변경으로 게시판 작성자 이름 변경
    public void updateBoardUserName(UserRequest request, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        List<BoardEntity> boardEntities = boardEntityRepository.findByUserId(userInfoDto.getUserId());

        for (BoardEntity boardEntity : boardEntities) {
            boardEntity.updateWriter(request.getName());
        }
    }
}
