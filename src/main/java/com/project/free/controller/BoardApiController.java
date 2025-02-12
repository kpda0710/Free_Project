package com.project.free.controller;

import com.project.free.dto.board.*;
import com.project.free.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping()
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest, Authentication authentication) {
        BoardResponse boardResponse = boardService.createBoard(boardRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }

    @GetMapping()
    public ResponseEntity<Page<BoardResponse>> getAllBoards(@RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Page<BoardResponse> boardResponseList = boardService.getAllBoards(page);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseList);
    }

    @GetMapping("/title")
    public ResponseEntity<Page<BoardResponse>> getBoardsByTitle(@RequestParam("title") String title, @RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        Page<BoardResponse> boardResponseList = boardService.getBoardsByTitle(title, page);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseList);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoardById(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        BoardDetailResponse boardResponse = boardService.getBoardByID(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }

    @GetMapping("/{boardId}/likes")
    public ResponseEntity<Integer> getCountLikes(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        Integer likeCount = boardService.getCountLikes(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(likeCount);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable(name = "boardId") Long boardId, @RequestBody BoardUpdateRequest boardRequest, Authentication authentication) {
        BoardResponse boardResponse = boardService.updateBoard(boardId, boardRequest, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable(name = "boardId") Long boardId, Authentication authentication) {
        boardService.deleteBoard(boardId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
