package com.project.free.dto.image;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class photoResponse {

    private Long photoId;

    private Long boardId;

    private String imagePath;
}
