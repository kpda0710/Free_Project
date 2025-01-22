package com.project.free.controller;

import com.project.free.dto.board.BoardCommentResponse;
import com.project.free.dto.board.BoardRequest;
import com.project.free.dto.board.BoardResponse;
import com.project.free.dto.board.BoardUpdateRequest;
import com.project.free.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping()
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest) {
        BoardResponse boardResponse = boardService.createBoard(boardRequest);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }

    @GetMapping()
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        List<BoardResponse> boardResponseList = boardService.getAllBoards();
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseList);
    }

    @GetMapping("/title")
    public ResponseEntity<List<BoardResponse>> getBoardsByTitle(@RequestParam("title") String title) {
        List<BoardResponse> boardResponseList = boardService.getBoardsByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseList);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardCommentResponse> getBoardById(@PathVariable(name = "boardId") Long boardId) {
        BoardCommentResponse boardResponse = boardService.getBoardByID(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable(name = "boardId") Long boardId, @RequestBody BoardUpdateRequest boardRequest) {
        BoardResponse boardResponse = boardService.updateBoard(boardId, boardRequest);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<BoardResponse> deleteBoard(@PathVariable(name = "boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
