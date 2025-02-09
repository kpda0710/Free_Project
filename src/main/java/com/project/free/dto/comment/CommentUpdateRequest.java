package com.project.free.dto.comment;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {

    private Long boardId;

    private String content;
}
