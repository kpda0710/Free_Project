package com.project.free.dto.like;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikesResponse {

    private Long likesId;

    private Long boardId;

    private Long userId;
}
