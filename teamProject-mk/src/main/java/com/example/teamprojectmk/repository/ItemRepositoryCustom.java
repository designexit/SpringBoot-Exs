package com.example.teamprojectmk.repository;


import com.example.teamprojectmk.dto.ItemSearchDto;
import com.example.teamprojectmk.dto.MainItemDto;
import com.example.teamprojectmk.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}