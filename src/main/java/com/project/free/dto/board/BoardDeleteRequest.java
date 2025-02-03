package com.project.free.dto.board;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDeleteRequest {

    private Long userId;
}
