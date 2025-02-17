package com.project.free.controller;

import com.project.free.dto.board.*;
import com.project.free.exception.ResponseCode;
import com.project.free.service.BoardService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping()
    public CustomResponse<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest, Authentication authentication) {
        BoardResponse boardResponse = boardService.createBoard(boardRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponse);
    }

    @GetMapping()
    public CustomResponse<Page<BoardResponse>> getAllBoards(@RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Page<BoardResponse> boardResponseList = boardService.getAllBoards(page);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponseList);
    }

    @GetMapping("/title")
    public CustomResponse<Page<BoardResponse>> getBoardsByTitle(@RequestParam("title") String title, @RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Page<BoardResponse> boardResponseList = boardService.getBoardsByTitle(title, page);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponseList);
    }

    @GetMapping("/{boardId}")
    public CustomResponse<BoardDetailResponse> getBoardById(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        BoardDetailResponse boardResponse = boardService.getBoardByID(boardId);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponse);
    }

    @GetMapping("/{boardId}/likes")
    public CustomResponse<Integer> getCountLikes(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        Integer likeCount = boardService.getCountLikes(boardId);
        return CustomResponse.success(ResponseCode.SUCCESS, likeCount);
    }

    @PutMapping("/{boardId}")
    public CustomResponse<BoardResponse> updateBoard(@PathVariable(name = "boardId") Long boardId, @RequestBody BoardUpdateRequest boardRequest, Authentication authentication) {
        BoardResponse boardResponse = boardService.updateBoard(boardId, boardRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponse);
    }

    @DeleteMapping("/{boardId}")
    public CustomResponse<Void> deleteBoard(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        boardService.deleteBoard(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }
}
