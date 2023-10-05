package com.example.ch8test.repository;

import com.example.ch8test.dto.ItemSearchDto;
import com.example.ch8test.dto.MainItemDto;
import com.example.ch8test.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}