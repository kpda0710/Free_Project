package com.project.free.controller;

import com.project.free.dto.like.LikesDeleteRequest;
import com.project.free.dto.like.LikesRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesApiController {

    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<LikesResponse> createLikes(@RequestBody LikesRequest likesRequest) {
        LikesResponse likesResponse = likesService.createLikes(likesRequest);
        return ResponseEntity.status(HttpStatus.OK).body(likesResponse);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteLikes(@RequestBody LikesDeleteRequest likesDeleteRequest) {
        likesService.deleteLikes(likesDeleteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
