package com.project.free.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
