package com.example.teamprojectmk.repository;

import com.example.teamprojectmk.dto.LectureSearchDto;
import com.example.teamprojectmk.dto.MainLectureDto;
import com.example.teamprojectmk.entity.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureRepositoryCustom {

    Page<Lecture> getAdminLecturePage(LectureSearchDto lectureSearchDto, Pageable pageable);

    Page<MainLectureDto> getMainLecturePage(LectureSearchDto lectureSearchDto, Pageable pageable);

}
