package com.project.free.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotNull
    private Long boardId;

    @NotBlank
    private String content;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{2,}$", message = "이름 형식에 맞지 않습니다. 2글자 이상, 영어 숫자가 들어갈 수 있습니다.")
    private String writer;
}
