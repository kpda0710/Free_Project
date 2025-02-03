package com.project.free.dto.board;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {

    private Long userId;

    private String title;

    private String content;

    private String writer;
}
