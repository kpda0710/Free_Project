package com.project.free.dto.comment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long commentId;

    private Long userId;

    private Long boardId;

    private String content;

    private String writer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentReplyResponse> reply;
}
