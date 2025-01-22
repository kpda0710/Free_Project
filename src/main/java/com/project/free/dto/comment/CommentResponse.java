package com.project.free.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long commentId;

    private Long boardId;

    private String comment;

    private String writer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
