package com.example.teamprojectmk.repository;

import com.example.teamprojectmk.entity.LectureImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureImgRepository extends JpaRepository<LectureImg, Long> {

    List<LectureImg> findByLectureIdOrderByIdAsc(Long lectureId);

}
