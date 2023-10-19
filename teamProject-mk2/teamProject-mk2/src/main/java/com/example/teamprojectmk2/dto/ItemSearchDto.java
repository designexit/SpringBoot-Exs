package com.example.teamprojectmk2.dto;

import com.example.teamprojectmk2.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    //private LectureStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

}