package com.project.free.controller;

import com.project.free.dto.board.*;
import com.project.free.exception.ResponseCode;
import com.project.free.service.BoardService;
import com.project.free.util.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // 게시판 생성 API
    @PostMapping()
    public CustomResponse<BoardResponse> createBoard(@RequestBody @Valid BoardRequest boardRequest, Authentication authentication) {
        BoardResponse boardResponse = boardService.createBoard(boardRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponse);
    }

    // 게시판 전체 가져오는 API
    @GetMapping()
    public CustomResponse<Page<BoardResponse>> getAllBoards(@RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Page<BoardResponse> boardResponseList = boardService.getAllBoards(page);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponseList);
    }

    // 제목 검색으로 게시판 가져오는 API
    @GetMapping("/title")
    public CustomResponse<Page<BoardResponse>> getBoardsByTitle(@RequestParam("title") String title, @RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Page<BoardResponse> boardResponseList = boardService.getBoardsByTitle(title, page, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponseList);
    }

    // 게시판 자세히 보는 API
    @GetMapping("/{boardId}")
    public CustomResponse<BoardDetailResponse> getBoardById(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        BoardDetailResponse boardResponse = boardService.getBoardByID(boardId);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponse);
    }

    // 좋아요 횟수 가져오는 API
    @GetMapping("/{boardId}/likes")
    public CustomResponse<Integer> getCountLikes(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        Integer likeCount = boardService.getCountLikes(boardId);
        return CustomResponse.success(ResponseCode.SUCCESS, likeCount);
    }

    // 게시판 수정하는 API
    @PutMapping("/{boardId}")
    public CustomResponse<BoardResponse> updateBoard(@PathVariable(name = "boardId") Long boardId, @RequestBody @Valid BoardUpdateRequest boardRequest, Authentication authentication) {
        BoardResponse boardResponse = boardService.updateBoard(boardId, boardRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, boardResponse);
    }

    // 게시판 삭제하는 API
    @DeleteMapping("/{boardId}")
    public CustomResponse<Void> deleteBoard(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        boardService.deleteBoard(boardId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, null);
    }
}
