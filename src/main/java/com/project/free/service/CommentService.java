package com.project.free.service;

import com.project.free.dto.comment.CommentReplyResponse;
import com.project.free.dto.comment.CommentRequest;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.comment.CommentUpdateRequest;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.dto.user.UserRequest;
import com.project.free.entity.BoardEntity;
import com.project.free.entity.CommentEntity;
import com.project.free.entity.ItemEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.BoardEntityRepository;
import com.project.free.repository.CommentEntityRepository;
import com.project.free.repository.ItemEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentEntityRepository commentEntityRepository;
    private final BoardEntityRepository boardEntityRepository;
    private final ItemEntityRepository itemEntityRepository;

    @Transactional
    // 댓글 생성(게시글)
    public CommentResponse createCommentByBoard(CommentRequest commentRequest, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        BoardEntity boardEntity = boardEntityRepository.findById(commentRequest.getTargetId()).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(userInfoDto.getUserId())
                .targetId(commentRequest.getTargetId())
                .content(commentRequest.getContent())
                .writer(userInfoDto.getName())
                .isDeleted(false)
                .build();

        List<CommentEntity> boardEntityComments = boardEntity.getComments();
        boardEntityComments.add(commentEntity);

        commentEntityRepository.save(commentEntity);
        boardEntityRepository.save(boardEntity);

        return CommentResponse.builder()
                .commentId(commentEntity.getCommentId())
                .userId(commentEntity.getUserId())
                .targetId(commentEntity.getTargetId())
                .content(commentEntity.getContent())
                .writer(commentEntity.getWriter())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }

    @Transactional
    // 댓글 생성(리뷰)
    public CommentResponse createCommentByItem(CommentRequest commentRequest, Authentication authentication) throws BaseException {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        ItemEntity itemEntity = itemEntityRepository.findById(commentRequest.getTargetId()).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(userInfoDto.getUserId())
                .targetId(commentRequest.getTargetId())
                .content(commentRequest.getContent())
                .writer(userInfoDto.getName())
                .isDeleted(false)
                .build();

        List<CommentEntity> comments = itemEntity.getComments();
        comments.add(commentEntity);

        commentEntityRepository.save(commentEntity);
        itemEntityRepository.save(itemEntity);

        return CommentResponse.builder()
                .commentId(commentEntity.getCommentId())
                .userId(commentEntity.getUserId())
                .targetId(commentEntity.getTargetId())
                .content(commentEntity.getContent())
                .writer(commentEntity.getWriter())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }

    @Transactional
    // 댓글 수정
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        CommentEntity commentEntity = getCommentEntity(commentId);

        CommentEntity updatedCommentEntity = CommentEntity.builder()
                .commentId(commentEntity.getCommentId())
                .targetId(commentEntity.getTargetId())
                .userId(commentEntity.getUserId())
                .content(commentUpdateRequest.getContent())
                .writer(commentEntity.getWriter())
                .reply(commentEntity.getReply())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isDeleted(commentEntity.getIsDeleted())
                .build();

        CommentEntity saved = commentEntityRepository.save(updatedCommentEntity);

        return CommentResponse.builder()
                .commentId(saved.getCommentId())
                .targetId(saved.getTargetId())
                .userId(saved.getUserId())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .reply(saved.getReply().stream().map(reply ->
                        CommentReplyResponse.builder()
                                .commentId(reply.getCommentId())
                                .userId(reply.getUserId())
                                .targetId(reply.getTargetId())
                                .content(reply.getContent())
                                .writer(reply.getWriter())
                                .createdAt(reply.getCreatedAt())
                                .updatedAt(reply.getUpdatedAt())
                                .build()).collect(Collectors.toList()))
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional
    // 댓글 삭제
    public void deleteComment(Long commentId) {
        CommentEntity commentEntity = getCommentEntity(commentId);
        BoardEntity boardEntity = boardEntityRepository.findById(commentEntity.getTargetId()).orElseThrow(() -> new BaseException(ResponseCode.BOARD_NOT_FOUND));

        commentEntity.deleteSetting();

        List<CommentEntity> boardEntityComments = boardEntity.getComments();
        boardEntityComments.remove(commentEntity);

        commentEntityRepository.save(commentEntity);
        boardEntityRepository.save(boardEntity);
    }

    @Transactional
    // 답글 생성
    public CommentReplyResponse createReply(Long commentId, CommentRequest commentRequest, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        CommentEntity reply = commentEntityRepository.findById(commentId).orElseThrow(() -> new BaseException(ResponseCode.COMMENT_NOT_FOUND));

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(userInfoDto.getUserId())
                .targetId(commentRequest.getTargetId())
                .content(commentRequest.getContent())
                .writer(userInfoDto.getName())
                .isDeleted(false)
                .build();

        CommentEntity saved = commentEntityRepository.save(commentEntity);

        reply.getReply().add(commentEntity);
        commentEntityRepository.save(reply);

        return CommentReplyResponse.builder()
                .commentId(saved.getCommentId())
                .userId(saved.getUserId())
                .targetId(saved.getTargetId())
                .content(saved.getContent())
                .writer(saved.getWriter())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // 댓글 아이디로 댓글 삭제 - 어드민 전용
    public void deleteCommentById(Long commentId, Authentication authentication) {
        CommentEntity commentEntity = getCommentEntity(commentId);
        commentEntity.deleteSetting();
        commentEntityRepository.save(commentEntity);
    }

    // CommentEntity 가져오기
    private CommentEntity getCommentEntity(Long commentId) {
        return commentEntityRepository.findById(commentId).orElseThrow(() -> new BaseException(ResponseCode.COMMENT_NOT_FOUND));
    }

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }

    @Transactional
    // 유저 이름 변경으로 댓글 작성자 이름 변경
    public void updateCommentUserName(UserRequest request, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        List<CommentEntity> commentEntities = commentEntityRepository.findByUserId(userInfoDto.getUserId());

        for (CommentEntity commentEntity : commentEntities) {
            commentEntity.updateWriter(request.getName());
        }
    }
}
