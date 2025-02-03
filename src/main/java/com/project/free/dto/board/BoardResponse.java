package com.project.free.dto.board;

import com.project.free.dto.comment.CommentResponse;
import com.project.free.entity.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    private Long boardId;

    private Long userId;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
