package com.example.teamprojectmk.controller;


import com.example.teamprojectmk.dto.ItemSearchDto;
import com.example.teamprojectmk.dto.LectureSearchDto;
import com.example.teamprojectmk.dto.MainItemDto;
import com.example.teamprojectmk.dto.MainLectureDto;
import com.example.teamprojectmk.service.ItemService;
import com.example.teamprojectmk.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){

        // 부트에서 페이징 처리를 쉽게 해 주는 인터페이스 기능
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    private final LectureService lectureService;

    @GetMapping(value = "/")
    public String main(LectureSearchDto lectureSearchDto, Optional<Integer> page, Model model){

        // 부트에서 페이징 처리를 쉽게 해 주는 인터페이스 기능
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainLectureDto> lectures = lectureService.getMainLecturePage(lectureSearchDto, pageable);

        model.addAttribute("lectures", lectures);
        model.addAttribute("lectureSearchDto", lectureSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }



}