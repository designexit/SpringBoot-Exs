package com.example.teamprojectmk2.repository;


import com.example.teamprojectmk2.entity.Item;
import com.example.teamprojectmk2.dto.ItemSearchDto;
import com.example.teamprojectmk2.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}