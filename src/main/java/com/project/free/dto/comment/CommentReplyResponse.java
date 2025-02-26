package com.project.free.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyResponse {

    private Long commentId;

    private Long userId;

    private Long targetId;

    private String content;

    private String writer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
