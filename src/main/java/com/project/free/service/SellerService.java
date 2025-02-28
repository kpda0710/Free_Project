package com.project.free.service;

import com.project.free.dto.seller.SellerRequest;
import com.project.free.dto.seller.SellerResponse;
import com.project.free.dto.seller.SellerUpdateRequest;
import com.project.free.dto.user.CustomUserDetails;
import com.project.free.dto.user.UserInfoDto;
import com.project.free.dto.user.UserRequest;
import com.project.free.entity.ItemEntity;
import com.project.free.entity.SellerEntity;
import com.project.free.exception.BaseException;
import com.project.free.exception.ResponseCode;
import com.project.free.repository.ItemEntityRepository;
import com.project.free.repository.SellerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerEntityRepository sellerEntityRepository;
    private final ItemEntityRepository itemEntityRepository;
    private final ItemService itemService;

    @Transactional
    // 유저 정보로 판매자 등록하기
    public SellerResponse registerSeller(SellerRequest sellerRequest, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        SellerEntity sellerEntity = SellerEntity.builder()
                .userId(userInfoDto.getUserId())
                .sellerName(userInfoDto.getName())
                .sellerEmail(userInfoDto.getEmail())
                .sellerPhone(sellerRequest.getSellerPhone())
                .sellerAddress(sellerRequest.getSellerAddress())
                .isDeleted(false)
                .build();

        SellerEntity saved = sellerEntityRepository.save(sellerEntity);

        return SellerResponse.builder()
                .sellerId(saved.getSellerId())
                .userId(saved.getUserId())
                .sellerName(saved.getSellerName())
                .sellerEmail(saved.getSellerEmail())
                .sellerPhone(saved.getSellerPhone())
                .sellerAddress(saved.getSellerAddress())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Transactional
    // 판매자 업데이트 (유저 정보랑은 별개)
    public SellerResponse updateSeller(SellerUpdateRequest sellerUpdateRequest, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);

        SellerEntity sellerEntity = getSellerEntity(userInfoDto);
        sellerEntity.updateSeller(sellerUpdateRequest);

        return SellerResponse.builder()
                .sellerId(sellerEntity.getSellerId())
                .userId(sellerEntity.getUserId())
                .sellerName(sellerEntity.getSellerName())
                .sellerEmail(sellerEntity.getSellerEmail())
                .sellerPhone(sellerEntity.getSellerPhone())
                .sellerAddress(sellerEntity.getSellerAddress())
                .createdAt(sellerEntity.getCreatedAt())
                .updatedAt(sellerEntity.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    // 판매자 정보 가져오기
    public SellerResponse getSeller(Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        SellerEntity sellerEntity = getSellerEntity(userInfoDto);

        return SellerResponse.builder()
                .sellerId(sellerEntity.getSellerId())
                .userId(sellerEntity.getUserId())
                .sellerName(sellerEntity.getSellerName())
                .sellerEmail(sellerEntity.getSellerEmail())
                .sellerPhone(sellerEntity.getSellerPhone())
                .sellerAddress(sellerEntity.getSellerAddress())
                .createdAt(sellerEntity.getCreatedAt())
                .updatedAt(sellerEntity.getUpdatedAt())
                .build();
    }

    @Transactional
    // 판매자 정보 삭제
    public void deleteSeller(Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        SellerEntity sellerEntity = getSellerEntity(userInfoDto);

        List<ItemEntity> itemEntityList = itemEntityRepository.findBySellerId(sellerEntity.getSellerId());
        if (!itemEntityList.isEmpty()) {
            for (ItemEntity itemEntity : itemEntityList) {
                itemService.deleteItem(itemEntity.getItemId(), authentication);
            }
        }
        sellerEntity.deleteSetting();
    }

    @Transactional
    // 유저 업데이트로 판매자 자동 업데이트
    public void updateSellerUserNameAndUserEmail(UserRequest request, Authentication authentication) {
        UserInfoDto userInfoDto = getUserInfoDto(authentication);
        SellerEntity sellerEntity = getSellerEntity(userInfoDto);

        SellerUpdateRequest sellerUpdateRequest = SellerUpdateRequest.builder()
                .sellerName(request.getName())
                .sellerEmail(request.getEmail())
                .sellerPhone(sellerEntity.getSellerPhone())
                .sellerAddress(sellerEntity.getSellerAddress())
                .build();

        sellerEntity.updateSeller(sellerUpdateRequest);
    }

    // 판매자 엔티티 가져오기
    private SellerEntity getSellerEntity(UserInfoDto userInfoDto) {
        return sellerEntityRepository.findByUserId(userInfoDto.getUserId()).orElseThrow(() -> new BaseException(ResponseCode.USER_NOT_FOUND));
    }

    // 인증 정보로 유저 데이터 가져오기
    private static UserInfoDto getUserInfoDto(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUserInfoDto();
    }
}
