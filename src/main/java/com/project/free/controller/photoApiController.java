package com.project.free.controller;

import com.project.free.dto.image.photoResponse;
import com.project.free.service.photoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class photoApiController {

    private final photoService photoService;

    @PostMapping("{boardId}")
    public ResponseEntity<List<photoResponse>> uploadImages(@PathVariable(name = "boardId") Long boardId, @RequestParam("images") List<MultipartFile> images, Authentication authentication) {
        List<photoResponse> photoRespons = photoService.uploadImage(boardId, images, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(photoRespons);
    }
}
