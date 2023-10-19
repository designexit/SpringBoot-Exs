package com.example.teamprojectmk3.repository;

import com.example.teamprojectmk3.entity.Item;
import com.example.teamprojectmk3.dto.ItemSearchDto;
import com.example.teamprojectmk3.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}