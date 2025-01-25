package com.project.free.controller;

import com.project.free.dto.like.LikesRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<LikesResponse> createLikes(@RequestBody LikesRequest likesRequest) {
        LikesResponse likesResponse = likesService.createLikes(likesRequest);
        return ResponseEntity.status(HttpStatus.OK).body(likesResponse);
    }

    @DeleteMapping("{likesId}")
    public ResponseEntity<Void> deleteLikes(@PathVariable(name = "likesId") Long likesId) {
        likesService.deleteLikes(likesId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
