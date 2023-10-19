package com.example.teamprojectmk.dto;

import com.querydsl.core.annotations.QueryProjection;

public class MainLectureDto {

    private Long id;

    private String lectureNm;

    private String lectureDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public MainLectureDto(Long id, String lectureNm, String lectureDetail, String imgUrl,Integer price){
        this.id = id;
        this.lectureNm = lectureNm;
        this.lectureDetail = lectureDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
