package com.example.teamprojectmk.controller;

import com.example.teamprojectmk.dto.LectureFormDto;
import com.example.teamprojectmk.dto.LectureSearchDto;
import com.example.teamprojectmk.entity.Lecture;
import com.example.teamprojectmk.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @GetMapping(value = "/admin/lecture/new")
    public String lectureForm(Model model){
        model.addAttribute("lectureFormDto", new LectureFormDto());
        return "lecture/lectureForm";
    }

    @PostMapping(value = "/admin/lecture/new")
    public String lectureNew(@Valid LectureFormDto lectureFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("lectureImgFile") List<MultipartFile> lectureImgFileList){

        if(bindingResult.hasErrors()){
            return "lecture/lectureForm";
        }

        if(lectureImgFileList.get(0).isEmpty() && LectureFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 강의 이미지는 필수 입력 값 입니다.");
            return "lecture/lectureForm";
        }

        try {
            lectureService.saveLecture(lectureFormDto, lectureImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "강의 등록 중 에러가 발생하였습니다.");
            return "lecture/lectureForm";
        }

        return "redirect:/";
    }

    // 상품 상세 페이지 폼
    @GetMapping(value = "/admin/lecture/{lectureId}")
    public String lectureDtl(@PathVariable("lectureId") Long lectureId, Model model){

        try {
            // 강의 번호로 실제 디비 조회 후 내용을 DTO에 담기
            LectureFormDto lectureFormDto = lectureService.getLectureDtl(lectureId);
            // 담은 DTO를 뷰로 전달
            model.addAttribute("lectureFormDto", lectureFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new LectureFormDto());
            return "lecture/lectureForm";
        }

        return "lecture/lectureForm";
    }

    // 강의 상세 페이지 처리
    @PostMapping(value = "/admin/lecture/{lectureId}")
    public String lectureUpdate(@Valid LectureFormDto lectureFormDto, BindingResult bindingResult,
                             @RequestParam("lectureImgFile") List<MultipartFile> lectureImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "lecture/lectureForm";
        }

        if(lectureImgFileList.get(0).isEmpty() && lectureFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 강의 이미지는 필수 입력 값 입니다.");
            return "lecture/lectureForm";
        }

        try {
            lectureService.updateLecture(lectureFormDto, lectureImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "강의 수정 중 에러가 발생하였습니다.");
            return "lecture/lectureForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/lectures", "/admin/lectures/{page}"})
    public String lectureManage(LectureSearchDto lectureSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<Lecture> lectures = lectureService.getAdminLecturePage(lectureSearchDto, pageable);

        model.addAttribute("lectures", lectures);
        model.addAttribute("lectureSearchDto", lectureSearchDto);
        model.addAttribute("maxPage", 5);

        return "lecture/lectureMng";
    }

    @GetMapping(value = "/lecture/{lectureId}")
    public String lectureDtl(Model model, @PathVariable("lectureId") Long lectureId){
        LectureFormDto lectureFormDto = lectureService.getLectureDtl(lectureId);
        model.addAttribute("lecture", lectureFormDto);
        return "lecture/lectureDtl";
    }
}
