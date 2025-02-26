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

    // 좋아요 생성 API
    @PostMapping("/board/{boardId}")
    public CustomResponse<LikesResponse> createLikesByBoard(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        LikesResponse likesResponse = likesService.createLikesByBoard(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, likesResponse);
    }

    // 좋아요 생성 API
    @PostMapping("/item/{itemId}")
    public CustomResponse<LikesResponse> createLikesByItem(@PathVariable(name = "itemId") Long boardId, Authentication authentication) {
        LikesResponse likesResponse = likesService.createLikesByItem(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, likesResponse);
    }

    // 좋아요 삭제 API
    @DeleteMapping("/board/{boardId}")
    public CustomResponse<Void> deleteLikesByBoard(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        likesService.deleteLikesByBoard(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }

    // 좋아요 삭제 API
    @DeleteMapping("/item/{itemId}")
    public CustomResponse<Void> deleteLikesByItem(@PathVariable(name = "itemId") Long boardId, Authentication authentication) {
        likesService.deleteLikesByItem(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }
}
