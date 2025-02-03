package com.project.free.controller;

import com.project.free.dto.comment.CommentDeleteRequest;
import com.project.free.dto.comment.CommentRequest;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.comment.CommentUpdateRequest;
import com.project.free.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createComment(commentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        CommentResponse commentResponse = commentService.updateComment(commentId, commentUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@RequestBody CommentDeleteRequest commentDeleteRequest) {
        commentService.deleteComment(commentDeleteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
