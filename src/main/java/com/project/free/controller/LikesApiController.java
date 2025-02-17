package com.project.free.controller;

import com.project.free.dto.like.LikesResponse;
import com.project.free.exception.ResponseCode;
import com.project.free.service.LikesService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesApiController {

    private final LikesService likesService;

    @PostMapping("{boardId}")
    public CustomResponse<LikesResponse> createLikes(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        LikesResponse likesResponse = likesService.createLikes(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, likesResponse);
    }

    @DeleteMapping("{boardId}")
    public CustomResponse<Void> deleteLikes(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        likesService.deleteLikes(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }
}
