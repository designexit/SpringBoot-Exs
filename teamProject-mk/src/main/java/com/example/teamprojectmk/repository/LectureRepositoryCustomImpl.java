package com.example.teamprojectmk.repository;

import com.example.teamprojectmk.constant.LectureStatus;
import com.example.teamprojectmk.dto.LectureSearchDto;
import com.example.teamprojectmk.dto.MainLectureDto;
import com.example.teamprojectmk.dto.QMainLectureDto;
import com.example.teamprojectmk.entity.Lecture;
import com.example.teamprojectmk.entity.QLecture;
import com.example.teamprojectmk.entity.QLectureImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class LectureRepositoryCustomImpl implements LectureRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public LectureRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchLectureStatusEq(LectureStatus searchLectureStatus){
        return searchLectureStatus == null ? null : QLecture.lecture.lectureStatus.eq(searchLectureStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QLecture.lecture.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("lectureNm", searchBy)){
            return QLecture.lecture.lectureNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QLecture.lecture.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Lecture> getAdminLecturePage(LectureSearchDto lectureSearchDto, Pageable pageable) {

        QueryResults<Lecture> results = queryFactory
                .selectFrom(QLecture.lecture)
                .where(regDtsAfter(lectureSearchDto.getSearchDateType()),
                        searchLectureStatusEq(lectureSearchDto.getSearchLectureStatus()),
                        searchByLike(lectureSearchDto.getSearchBy(),
                                lectureSearchDto.getSearchQuery()))
                .orderBy(QLecture.lecture.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Lecture> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression lectureNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QLecture.lecture.lectureNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainLectureDto> getMainLecturePage(LectureSearchDto lectureSearchDto, Pageable pageable) {
        Lecture lecture = Lecture.lecture;
        QLectureImg lectureImg = QLectureImg.lectureImg;

        QueryResults<MainLectureDto> results = queryFactory
                .select(
                        // QueryProjection의 생성자를 이용해 바로 검색 조건으로 자동 매핑
                        new QMainLectureDto(
                                lecture.id,
                                lecture.lectureNm,
                                lecture.lectureDetail,
                                lectureImg.imgUrl,
                                lecture.price)
                )
                .from(lectureImg)
                .join(lectureImg.lecture, lecture)
                .where(lectureImg.repimgYn.eq("Y"))
                .where(lectureNmLike(lectureSearchDto.getSearchQuery()))
                .orderBy(lecture.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainLectureDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


}
