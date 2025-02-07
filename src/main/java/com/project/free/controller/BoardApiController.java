package com.project.free.controller;

import com.project.free.dto.board.*;
import com.project.free.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<BoardResponse>> getAllBoards(Authentication authentication) {
        List<BoardResponse> boardResponseList = boardService.getAllBoards();
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseList);
    }

    @GetMapping("/title")
    public ResponseEntity<List<BoardResponse>> getBoardsByTitle(@RequestParam("title") String title, Authentication authentication) {
        List<BoardResponse> boardResponseList = boardService.getBoardsByTitle(title);
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
