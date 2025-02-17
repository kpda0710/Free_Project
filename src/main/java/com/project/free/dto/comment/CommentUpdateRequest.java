package com.project.free.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {

    @NotNull
    private Long boardId;

    @NotBlank
    private String content;
}
