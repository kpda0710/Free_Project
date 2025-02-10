package com.project.free.controller;

import com.project.free.dto.like.LikesDeleteRequest;
import com.project.free.dto.like.LikesRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesApiController {

    private final LikesService likesService;

    @PostMapping("{boardId}")
    public ResponseEntity<LikesResponse> createLikes(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        LikesResponse likesResponse = likesService.createLikes(boardId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(likesResponse);
    }

    @DeleteMapping("{boardId}")
    public ResponseEntity<Void> deleteLikes(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        likesService.deleteLikes(boardId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
