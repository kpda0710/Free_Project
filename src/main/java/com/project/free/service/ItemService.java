package com.project.free.service;

import com.project.free.dto.image.PhotoResponse;
import com.project.free.dto.item.ItemDetailResponse;
import com.project.free.dto.item.ItemRequest;
import com.project.free.dto.item.ItemResponse;
import com.project.free.dto.item.ItemUpdateRequest;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.entity.*;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.ItemEntityRepository;
import com.project.free.repository.SellerEntityRepository;
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
                .itemCategory(itemRequest.getItemCategory())
                .isDeleted(false)
                .build();

        ItemEntity saved = itemEntityRepository.save(itemEntity);

        return ItemResponse.builder()
                .itemId(saved.getItemId())
                .sellerId(saved.getSellerId())
                .itemName(saved.getItemName())
                .itemPrice(saved.getItemPrice())
                .itemDescription(saved.getItemDescription())
                .itemCategory(saved.getItemCategory())
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
                .itemCategory(itemEntity.getItemCategory())
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
    public PageImpl<ItemDetailResponse> getItemByItemName(String itemName, int pageNumber, Authentication authentication) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(sorts));
        Page<ItemEntity> itemEntityList = itemEntityRepository.findByItemNameContaining(itemName, pageable);

        List<ItemDetailResponse> itemDetailResponseList = itemEntityList.stream().map(itemEntity ->
                ItemDetailResponse.builder()
                        .itemId(itemEntity.getItemId())
                        .sellerId(itemEntity.getSellerId())
                        .itemName(itemEntity.getItemName())
                        .itemPrice(itemEntity.getItemPrice())
                        .itemDescription(itemEntity.getItemDescription())
                        .itemCategory(itemEntity.getItemCategory())
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

        return new PageImpl<>(itemDetailResponseList, pageable, itemEntityList.getTotalElements());
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
                .itemCategory(itemEntity.getItemCategory())
                .createdAt(itemEntity.getCreatedAt())
                .updatedAt(itemEntity.getUpdatedAt())
                .build();
    }

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }
}
