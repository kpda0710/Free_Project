package com.project.free.controller;

import com.project.free.dto.image.photoResponse;
import com.project.free.exception.ResponseCode;
import com.project.free.service.photoService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
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
    public CustomResponse<List<photoResponse>> uploadImages(@PathVariable(name = "boardId") Long boardId, @RequestParam(name = "images") List<MultipartFile> images, Authentication authentication) {
        List<photoResponse> photoResponse = photoService.uploadImage(boardId, images, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, photoResponse);
    }
}
