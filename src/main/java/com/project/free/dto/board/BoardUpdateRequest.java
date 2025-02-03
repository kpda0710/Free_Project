package com.project.free.dto.board;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateRequest {

    private Long userId;

    private String title;

    private String content;
}
