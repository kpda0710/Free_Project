package com.project.free.dto.image;

import com.project.free.entity.PhotoStatus;
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

    private PhotoStatus photoStatus;
}
