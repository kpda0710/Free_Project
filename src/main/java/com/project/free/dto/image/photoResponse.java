package com.project.free.dto.image;

import com.project.free.entity.PhotoStatus;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponse {

    private Long photoId;

    private Long targetId;

    private String photoPath;

    private PhotoStatus photoStatus;
}
