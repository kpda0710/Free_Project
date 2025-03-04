package com.project.free.service;

import com.project.free.dto.comment.CommentReplyResponse;
import com.project.free.dto.comment.CommentResponse;
import com.project.free.dto.image.PhotoResponse;
import com.project.free.dto.item.ItemDetailResponse;
import com.project.free.dto.item.ItemResponse;
import com.project.free.dto.item.ItemRequest;
import com.project.free.dto.item.ItemUpdateRequest;
import com.project.free.dto.like.LikesResponse;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.entity.*;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.ItemEntityRepository;
import com.project.free.repository.SellerEntityRepository;
import com.project.free.repository.ShoppingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemEntityRepository itemEntityRepository;
    private final SellerEntityRepository sellerEntityRepository;
    private final ShoppingEntityRepository shoppingEntityRepository;

    @Transactional
    // 쇼핑몰에 상품 등록
    public ItemResponse registerItem(ItemRequest itemRequest, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        SellerEntity sellerEntity = sellerEntityRepository.findByUserId(userInfoDto.getUserId()).orElseThrow(() -> new BaseException(ResponseCode.USER_NOT_FOUND));

        ItemEntity itemEntity = ItemEntity.builder()
                .sellerId(sellerEntity.getSellerId())
                .itemName(itemRequest.getItemName())
                .itemPrice(itemRequest.getItemPrice())
                .itemDescription(itemRequest.getItemDescription())
                .quantity(itemRequest.getQuantity())
                .itemCategory(itemRequest.getItemCategory())
                .status(ItemStatus.WAITING)
                .isDeleted(false)
                .build();

        ItemEntity saved = itemEntityRepository.save(itemEntity);

        return ItemResponse.builder()
                .itemId(saved.getItemId())
                .sellerId(saved.getSellerId())
                .itemName(saved.getItemName())
                .itemPrice(saved.getItemPrice())
                .itemDescription(saved.getItemDescription())
                .quantity(saved.getQuantity())
                .itemCategory(saved.getItemCategory())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    // 상품 ID로 조회
    public ItemDetailResponse getItemById(Long itemId, Authentication authentication) {
        ItemEntity itemEntity = itemEntityRepository.findById(itemId).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));

        return ItemDetailResponse.builder()
                .itemId(itemEntity.getItemId())
                .sellerId(itemEntity.getSellerId())
                .itemName(itemEntity.getItemName())
                .itemPrice(itemEntity.getItemPrice())
                .itemDescription(itemEntity.getItemDescription())
                .quantity(itemEntity.getQuantity())
                .itemCategory(itemEntity.getItemCategory())
                .status(itemEntity.getStatus())
                .likes(itemEntity.getLikes().stream().map(likesEntity ->
                        LikesResponse.builder()
                                .likesId(likesEntity.getLikesId())
                                .targetId(likesEntity.getTargetId())
                                .userId(likesEntity.getUserId())
                                .build()).collect(Collectors.toList()))
                .comments(itemEntity.getComments().stream().map(commentEntity ->
                        CommentResponse.builder()
                                .commentId(commentEntity.getCommentId())
                                .userId(commentEntity.getUserId())
                                .targetId(commentEntity.getTargetId())
                                .content(commentEntity.getContent())
                                .writer(commentEntity.getWriter())
                                .createdAt(commentEntity.getCreatedAt())
                                .updatedAt(commentEntity.getUpdatedAt())
                                .reply(commentEntity.getReply().stream().map(reply ->
                                        CommentReplyResponse.builder()
                                                .commentId(reply.getCommentId())
                                                .userId(reply.getUserId())
                                                .targetId(reply.getTargetId())
                                                .content(reply.getContent())
                                                .writer(reply.getWriter())
                                                .createdAt(reply.getCreatedAt())
                                                .updatedAt(reply.getUpdatedAt())
                                                .build()).collect(Collectors.toList()))
                                .build()).collect(Collectors.toList()))
                .photos(itemEntity.getPhotos().stream().map(photoEntity ->
                        PhotoResponse.builder()
                                .photoId(photoEntity.getPhotoId())
                                .targetId(photoEntity.getTargetId())
                                .photoPath(photoEntity.getPhotoPath())
                                .photoStatus(photoEntity.getPhotoStatus())
                                .build()).collect(Collectors.toList()))
                .createdAt(itemEntity.getCreatedAt())
                .updatedAt(itemEntity.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    // 상품 이름으로 상품 조회
    public PageImpl<ItemResponse> getItemByItemName(String itemName, int pageNumber, Authentication authentication) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(sorts));
        Page<ItemEntity> itemEntityList = itemEntityRepository.findByItemNameContaining(itemName, pageable);

        List<ItemResponse> itemResponseList = itemEntityList.stream().map(itemEntity ->
                ItemResponse.builder()
                        .itemId(itemEntity.getItemId())
                        .sellerId(itemEntity.getSellerId())
                        .itemName(itemEntity.getItemName())
                        .itemPrice(itemEntity.getItemPrice())
                        .itemDescription(itemEntity.getItemDescription())
                        .quantity(itemEntity.getQuantity())
                        .itemCategory(itemEntity.getItemCategory())
                        .status(itemEntity.getStatus())
                        .photos(itemEntity.getPhotos().stream().map(photoEntity ->
                                PhotoResponse.builder()
                                        .photoId(photoEntity.getPhotoId())
                                        .targetId(photoEntity.getTargetId())
                                        .photoPath(photoEntity.getPhotoPath())
                                        .photoStatus(photoEntity.getPhotoStatus())
                                        .build()).collect(Collectors.toList()))
                        .createdAt(itemEntity.getCreatedAt())
                        .updatedAt(itemEntity.getUpdatedAt())
                        .build()).collect(Collectors.toList());

        return new PageImpl<>(itemResponseList, pageable, itemEntityList.getTotalElements());
    }

    @Transactional(readOnly = true)
    // 상품 카테고리로 조회
    public PageImpl<ItemResponse> getItemByCategory(ItemCategory itemCategory, int pageNumber, Authentication authentication) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(sorts));
        Page<ItemEntity> itemEntityList = itemEntityRepository.findByItemCategory(itemCategory, pageable);

        List<ItemResponse> itemResponseList = itemEntityList.stream().map(itemEntity ->
                ItemResponse.builder()
                        .itemId(itemEntity.getItemId())
                        .sellerId(itemEntity.getSellerId())
                        .itemName(itemEntity.getItemName())
                        .itemPrice(itemEntity.getItemPrice())
                        .itemDescription(itemEntity.getItemDescription())
                        .quantity(itemEntity.getQuantity())
                        .itemCategory(itemEntity.getItemCategory())
                        .status(itemEntity.getStatus())
                        .photos(itemEntity.getPhotos().stream().map(photoEntity ->
                                PhotoResponse.builder()
                                        .photoId(photoEntity.getPhotoId())
                                        .targetId(photoEntity.getTargetId())
                                        .photoPath(photoEntity.getPhotoPath())
                                        .photoStatus(photoEntity.getPhotoStatus())
                                        .build()).collect(Collectors.toList()))
                        .createdAt(itemEntity.getCreatedAt())
                        .updatedAt(itemEntity.getUpdatedAt())
                        .build()).collect(Collectors.toList());

        return new PageImpl<>(itemResponseList, pageable, itemEntityList.getTotalElements());
    }

    @Transactional(readOnly = true)
    // 상품 전체 조회
    public PageImpl<ItemResponse> getItemAll(int pageNumber, Authentication authentication) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(sorts));
        Page<ItemEntity> itemEntityList = itemEntityRepository.findAll(pageable);

        List<ItemResponse> itemResponseList = itemEntityList.stream().map(itemEntity ->
                ItemResponse.builder()
                        .itemId(itemEntity.getItemId())
                        .sellerId(itemEntity.getSellerId())
                        .itemName(itemEntity.getItemName())
                        .itemPrice(itemEntity.getItemPrice())
                        .itemDescription(itemEntity.getItemDescription())
                        .quantity(itemEntity.getQuantity())
                        .itemCategory(itemEntity.getItemCategory())
                        .status(itemEntity.getStatus())
                        .photos(itemEntity.getPhotos().stream().map(photoEntity ->
                                PhotoResponse.builder()
                                        .photoId(photoEntity.getPhotoId())
                                        .targetId(photoEntity.getTargetId())
                                        .photoPath(photoEntity.getPhotoPath())
                                        .photoStatus(photoEntity.getPhotoStatus())
                                        .build()).collect(Collectors.toList()))
                        .createdAt(itemEntity.getCreatedAt())
                        .updatedAt(itemEntity.getUpdatedAt())
                        .build()).collect(Collectors.toList());

        return new PageImpl<>(itemResponseList, pageable, itemEntityList.getTotalElements());
    }

    @Transactional
    // 상품 수정
    public ItemResponse updateItem(Long itemId, ItemUpdateRequest itemUpdateRequest, Authentication authentication) {
        ItemEntity itemEntity = itemEntityRepository.findById(itemId).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));

        itemEntity.updateItemName(itemUpdateRequest.getItemName());
        itemEntity.updatePrice(itemUpdateRequest.getItemPrice());
        itemEntity.updateDescription(itemUpdateRequest.getItemDescription());
        itemEntity.updateCategory(itemUpdateRequest.getItemCategory());
        itemEntityRepository.save(itemEntity);

        return ItemResponse.builder()
                .itemId(itemEntity.getItemId())
                .sellerId(itemEntity.getSellerId())
                .itemName(itemEntity.getItemName())
                .itemPrice(itemEntity.getItemPrice())
                .itemDescription(itemEntity.getItemDescription())
                .quantity(itemEntity.getQuantity())
                .itemCategory(itemEntity.getItemCategory())
                .status(itemEntity.getStatus())
                .createdAt(itemEntity.getCreatedAt())
                .updatedAt(itemEntity.getUpdatedAt())
                .build();
    }

    // 상품 삭제
    @Transactional
    public void deleteItem(Long itemId, Authentication authentication) {
        ItemEntity itemEntity = itemEntityRepository.findById(itemId).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));

        itemEntity.getLikes().forEach(like -> like.deleteSetting());
        itemEntity.getComments().forEach(comment -> {
            comment.deleteSetting();
            comment.getReply().forEach(reply -> reply.deleteSetting());
        });
        itemEntity.getPhotos().forEach(photo -> photo.deleteSetting());
        itemEntity.deleteSetting();
    }

    @Transactional(readOnly = true)
    public void buyItem(Long itemId, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        ItemEntity itemEntity = itemEntityRepository.findByItemIdAndStatus(itemId, ItemStatus.SOLD).orElseThrow(() -> new BaseException(ResponseCode.ITEM_NOT_FOUND));

        ShoppingEntity shoppingEntity = shoppingEntityRepository.findByUserId(userInfoDto.getUserId()).orElseThrow(() -> new BaseException(ResponseCode.USER_NOT_FOUND));
        if (shoppingEntity.getMoney() >= itemEntity.getItemPrice() && itemEntity.getQuantity() > 0) {
            shoppingEntity.minusMoney(itemEntity.getItemPrice());
            shoppingEntity.plusTotalUsedMoney(itemEntity.getItemPrice());
            shoppingEntity.plusPoint(itemEntity.getItemPrice() * (shoppingEntity.getStatus().getPointRate() / 100));
            updateStatus(shoppingEntity);
        } else {
            throw new BaseException(ResponseCode.ITEM_BUY_FAIL);
        }
    }

    private void updateStatus(ShoppingEntity shoppingEntity) {
        if (shoppingEntity.getTotalUsedMoney() >= ShoppingStatus.VIP.getGradeMax()) {
            shoppingEntity.updateStatus(ShoppingStatus.VIP);
        } else if (shoppingEntity.getTotalUsedMoney() >= ShoppingStatus.DIAMOND.getGradeMax()) {
            shoppingEntity.updateStatus(ShoppingStatus.DIAMOND);
        } else if (shoppingEntity.getTotalUsedMoney() >= ShoppingStatus.GOLD.getGradeMax()) {
            shoppingEntity.updateStatus(ShoppingStatus.GOLD);
        } else if (shoppingEntity.getTotalUsedMoney() >= ShoppingStatus.SILVER.getGradeMax()) {
            shoppingEntity.updateStatus(ShoppingStatus.SILVER);
        }
    }

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }
}
