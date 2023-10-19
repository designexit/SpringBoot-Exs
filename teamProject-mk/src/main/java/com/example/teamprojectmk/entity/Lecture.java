package com.example.teamprojectmk.entity;

import com.example.teamprojectmk.constant.LectureStatus;
import com.example.teamprojectmk.dto.LectureFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="lecture")
@Getter
@Setter
@ToString
public class Lecture extends BaseEntity {
    @Id
    @Column(name="lecture_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;       //강의 코드

    @Column(nullable = false, length = 50)
    private String lectureNm; //강의명

    @Column(name="price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private String address; //주소

    @Lob
    @Column(nullable = false)
    private String lectureDetail; //강의 소개

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus; //강의 타입

    public void updateLecture(LectureFormDto lectureFormDto){
        this.lectureNm = lectureFormDto.getLectureNm();
        this.price = lectureFormDto.getPrice();
        this.address = lectureFormDto.getAddress();
        this.lectureDetail = lectureFormDto.getLectureDetail();
        this.lectureStatus = lectureFormDto.getLectureStatus();
    }
}
