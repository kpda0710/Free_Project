package com.project.free.dto.board;

import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.image.PhotoResponse;
import com.project.free.dto.like.LikesResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailResponse {

    private Long boardId;

    private Long userId;

    private String title;

    private String content;

    private String writer;

    private Long views;

    private List<CommentResponse> comments;

    private List<LikesResponse> likes;

    private List<PhotoResponse> photo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
