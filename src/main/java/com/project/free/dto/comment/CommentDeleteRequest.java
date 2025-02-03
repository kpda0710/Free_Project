package com.project.free.dto.comment;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeleteRequest {

    private Long userId;

    private Long boardId;

    private Long commentId;
}
