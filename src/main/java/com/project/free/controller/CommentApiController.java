package com.project.free.controller;

import com.project.free.dto.comment.*;
import com.project.free.exception.ResponseCode;
import com.project.free.service.CommentService;
import com.project.free.util.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    // 댓글 생성하는 API
    @PostMapping()
    public CustomResponse<CommentResponse> createComment(@RequestBody @Valid CommentRequest commentRequest, Authentication authentication) {
        CommentResponse commentResponse = commentService.createComment(commentRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, commentResponse);
    }

    // 댓글 or 답글 수정하는 API
    @PutMapping("{commentId}")
    public CustomResponse<CommentResponse> updateComment(@PathVariable(name = "commentId") Long commentId, @RequestBody @Valid CommentUpdateRequest commentUpdateRequest, Authentication authentication) {
        CommentResponse commentResponse = commentService.updateComment(commentId, commentUpdateRequest);
        return CustomResponse.success(ResponseCode.SUCCESS, commentResponse);
    }

    // 댓글 or 답글 삭제하는 API
    @DeleteMapping("{commentId}")
    public CustomResponse<Void> deleteComment(@PathVariable(name = "commentId") Long commentId, Authentication authentication) {
        commentService.deleteComment(commentId);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }

    // 답글 생성하는 API
    @PostMapping("/{commentId}")
    public CustomResponse<CommentReplyResponse> replyComment(@PathVariable(name = "commentId") Long commentId, @RequestBody @Valid CommentRequest commentRequest, Authentication authentication) {
        CommentReplyResponse reply = commentService.createReply(commentId, commentRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, reply);
    }
}
