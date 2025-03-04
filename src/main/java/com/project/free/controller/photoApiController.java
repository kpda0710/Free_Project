package com.project.free.controller;

import com.project.free.dto.image.PhotoResponse;
import com.project.free.exception.ResponseCode;
import com.project.free.service.PhotoService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class PhotoApiController {

    private final PhotoService photoService;

    // 게시판 사진 게시 API
    @PostMapping("/board/{boardId}")
        public CustomResponse<List<PhotoResponse>> uploadImagesByBoard(@PathVariable(name = "boardId") Long boardId, @RequestParam(name = "images") List<MultipartFile> images, Authentication authentication) {
        List<PhotoResponse> photoResponse = photoService.uploadImageByBoard(boardId, images, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, photoResponse);
    }

    @PostMapping("/item/{itemId}")
    public CustomResponse<List<PhotoResponse>> uploadImagesByItem(@PathVariable(name = "itemId") Long itemId, @RequestParam(name = "images") List<MultipartFile> images, Authentication authentication) {
        List<PhotoResponse> photoResponse = photoService.uploadImageByItem(itemId, images, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, photoResponse);
    }


    // 사진 삭제 API
    @DeleteMapping("{photoId}")
    public CustomResponse<String> deleteImage(@PathVariable(name = "photoId") Long photoId, Authentication authentication) {
        photoService.deleteImage(photoId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }
}
