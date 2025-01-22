package com.project.free.dto.board;

import com.project.free.dto.comment.CommentResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentResponse {

    private Long boardId;

    private String title;

    private String content;

    private String writer;

    private List<CommentResponse> commentResponse;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
