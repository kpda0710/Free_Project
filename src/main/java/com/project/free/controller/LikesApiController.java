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

    @PostMapping
    public ResponseEntity<LikesResponse> createLikes(@RequestBody LikesRequest likesRequest, Authentication authentication) {
        LikesResponse likesResponse = likesService.createLikes(likesRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(likesResponse);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteLikes(@RequestBody LikesDeleteRequest likesDeleteRequest, Authentication authentication) {
        likesService.deleteLikes(likesDeleteRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
