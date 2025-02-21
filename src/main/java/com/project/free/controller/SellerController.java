package com.project.free.controller;

import com.project.free.dto.seller.SellerRequest;
import com.project.free.dto.seller.SellerResponse;
import com.project.free.dto.seller.SellerUpdateRequest;
import com.project.free.exception.ResponseCode;
import com.project.free.service.SellerService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    // 유저 정보로 판매자 등록
    @PostMapping
    public CustomResponse<SellerResponse> registerSeller(@RequestBody SellerRequest sellerRequest, Authentication authentication) {
        SellerResponse sellerResponse = sellerService.registerSeller(sellerRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, sellerResponse);
    }

    // 판매자 정보 수정
    @PutMapping
    public CustomResponse<SellerResponse> updateSeller(@RequestBody SellerUpdateRequest sellerUpdateRequest, Authentication authentication) {
        SellerResponse sellerResponse = sellerService.updateSeller(sellerUpdateRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, sellerResponse);
    }

    // 판매자 정보 조회
    @GetMapping
    public CustomResponse<SellerResponse> getSeller(Authentication authentication) {
        SellerResponse sellerResponse = sellerService.getSeller(authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, sellerResponse);
    }
}
