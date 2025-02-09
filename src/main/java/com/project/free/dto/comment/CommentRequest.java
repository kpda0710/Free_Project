package com.project.free.dto.comment;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private Long boardId;

    private String content;

    private String writer;
}
