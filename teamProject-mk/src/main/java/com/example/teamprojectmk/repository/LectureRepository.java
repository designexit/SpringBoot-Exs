package com.example.teamprojectmk.repository;

import com.example.teamprojectmk.entity.Item;
import com.example.teamprojectmk.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long>,
        QuerydslPredicateExecutor<Lecture>, LectureRepositoryCustom {

    List<Lecture> findByLectureNm(String lectureNm);

    List<Lecture> findByLectureNmOrLectureDetail(String lectureNm, String lectureDetail);

    List<Lecture> findByPriceLessThan(Integer price);

    List<Lecture> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Lecture i where i.lectureDetail like " +
            "%:lectureDetail% order by i.price desc")
    List<Lecture> findByLectureDetail(@Param("lectureDetail") String lectureDetail);

    @Query(value="select * from lecture i where i.lecture_detail like " +
            "%:lectureDetail% order by i.price desc", nativeQuery = true)
    List<Lecture> findByItemDetailByNative(@Param("lectureDetail") String lectureDetail);

}
