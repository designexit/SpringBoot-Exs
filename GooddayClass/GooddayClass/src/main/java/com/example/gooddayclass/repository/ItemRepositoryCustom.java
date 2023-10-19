package com.example.gooddayclass.repository;

import com.example.gooddayclass.dto.ItemSearchDto;
import com.example.gooddayclass.dto.MainItemDto;
import com.example.gooddayclass.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}