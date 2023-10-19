package com.example.teamprojectmk.dto;

import com.example.teamprojectmk.constant.LectureStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LectureDto {

    private Long id;

    private String lectureNm;

    private Integer price;

    private String address;

    private String category;

    private LectureStatus lectureStatus;

    private LocalDateTime beginDateLectureTime;

    private LocalDateTime endLectureDateTime;

    private String lectureDetail;

    //private LocalDateTime regTime;

    //private LocalDateTime updateTime;
}
