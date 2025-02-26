package com.project.free.controller;

import com.project.free.dto.item.ItemDetailResponse;
import com.project.free.dto.item.ItemResponse;
import com.project.free.dto.item.ItemRequest;
import com.project.free.dto.item.ItemUpdateRequest;
import com.project.free.entity.ItemCategory;
import com.project.free.exception.ResponseCode;
import com.project.free.service.ItemService;
import com.project.free.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    public CustomResponse<ItemResponse> registerItem(@RequestBody ItemRequest itemRequest, Authentication authentication) {
        ItemResponse itemResponse = itemService.registerItem(itemRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, itemResponse);
    }

    @GetMapping("{itemId}")
    public CustomResponse<ItemDetailResponse> getItemById(@PathVariable(name = "itemId") Long itemId, Authentication authentication) {
        ItemDetailResponse itemResponse = itemService.getItemById(itemId, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, itemResponse);
    }

    @GetMapping("/title")
    public CustomResponse<PageImpl<ItemResponse>> getItemByItemName(@RequestParam("itemName") String itemName, @RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        PageImpl<ItemResponse> itemDetailResponse = itemService.getItemByItemName(itemName, page, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, itemDetailResponse);
    }

    @GetMapping("/category")
    public CustomResponse<PageImpl<ItemResponse>> getItemByCategory(@RequestParam("category") ItemCategory category, @RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        PageImpl<ItemResponse> itemDetailResponse = itemService.getItemByCategory(category, page, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, itemDetailResponse);
    }

    @GetMapping()
    public CustomResponse<PageImpl<ItemResponse>> getItemAll(@RequestParam(name = "page", defaultValue = "0") int page, Authentication authentication) {
        PageImpl<ItemResponse> itemDetailResponseList = itemService.getItemAll(page, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, itemDetailResponseList);
    }

    @PutMapping("{itemId}")
    public CustomResponse<ItemResponse> updateItem(@PathVariable(name = "itemId") Long itemId, @RequestBody ItemUpdateRequest itemUpdateRequest, Authentication authentication) {
        ItemResponse itemResponse = itemService.updateItem(itemId, itemUpdateRequest, authentication);
        return CustomResponse.success(ResponseCode.SUCCESS, itemResponse);
    }
}
