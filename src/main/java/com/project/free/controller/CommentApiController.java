package com.project.free.controller;

import com.project.free.dto.comment.*;
import com.project.free.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest, Authentication authentication) {
        CommentResponse commentResponse = commentService.createComment(commentRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest, Authentication authentication) {
        CommentResponse commentResponse = commentService.updateComment(commentId, commentUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "commentId") Long commentId, Authentication authentication) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<CommentReplyResponse> replyComment(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentRequest commentRequest, Authentication authentication) {
        CommentReplyResponse reply = commentService.createReply(commentId, commentRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(reply);
    }
}
