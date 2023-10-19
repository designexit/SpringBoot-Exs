package com.example.teamprojectmk.dto;

import com.example.teamprojectmk.constant.LectureStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureSearchDto {

    private String searchDateType;

    private LectureStatus searchLectureStatus;

    private String searchBy;

    private String searchQuery = "";
}
