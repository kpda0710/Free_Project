package com.project.free.dto.item;

import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.image.PhotoResponse;
import com.project.free.dto.like.LikesResponse;
import com.project.free.entity.CommentEntity;
import com.project.free.entity.ItemCategory;
import com.project.free.entity.LikesEntity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetailResponse {

    private Long itemId;

    private Long sellerId;

    private String itemName;

    private BigDecimal itemPrice;

    private String itemDescription;

    private ItemCategory itemCategory;

    private List<LikesResponse> likes = new ArrayList<>();

    private List<CommentResponse> comments = new ArrayList<>();

    private List<PhotoResponse> photos = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
