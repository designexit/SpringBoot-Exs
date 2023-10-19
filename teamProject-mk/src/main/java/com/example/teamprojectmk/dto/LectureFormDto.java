package com.example.teamprojectmk.dto;

import com.example.teamprojectmk.constant.LectureStatus;
import com.example.teamprojectmk.entity.Lecture;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LectureFormDto {

    private Long id;

    @NotBlank(message = "강의 명은 필수 입력 값입니다.")
    private String lectureNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "주소는 필수 입력 값입니다.") // 주소
    private String address;

    @NotNull(message = "카테고리는 필수 입력 값입니다.") // 카테고리
    private String category;

    @NotNull(message = "강의 타입은 필수 입력 값입니다.") // 강의 타입
    private LectureStatus lectureStatus;

    @NotNull(message = "강의 시작일은 필수 입력 값입니다.")
    private LocalDateTime beginDateLectureTime; // 걍의 시작일

    @NotNull(message = "강의 종료일은 필수 입력 값입니다.")
    private LocalDateTime endLectureDateTime; // 걍의 종료일

    @NotBlank(message = "강의 소개는 필수 입력 값입니다.") // 강의 소개
    private String lectureDetail;

    private List<LectureImgDto> lectureImgDtoList = new ArrayList<>();

    private List<Long> lectureImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();


    public Lecture createLecture(){
        return modelMapper.map(this, Lecture.class);
    }

    public static LectureFormDto of(Lecture lecture){
        return modelMapper.map(lecture,LectureFormDto.class);
    }
}
