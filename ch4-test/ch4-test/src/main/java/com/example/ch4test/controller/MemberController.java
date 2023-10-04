package com.example.ch4test.controller;


import com.example.ch4test.dto.MemberFormDto;
import com.example.ch4test.entity.Member;
import com.example.ch4test.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //회원가입 폼
    @GetMapping(value = "/new")
    public String memberForm(Model model){
        // 서버->뷰 데이터 전달
        model.addAttribute("memberFormDto", new MemberFormDto());
        // 뷰 리졸버, 타임리프 사용해서 회원가입 폼 html전달
        return "member/memberForm";
    }

    //회원가입 처리
    @PostMapping(value = "/new")
    // 유효성 체크, 값의 유무
    //bindingResult -> 유효성 체크에서 오류 발생시 메세지 확인용도
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            System.out.println("bindingResult : " + bindingResult.toString());
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    // 로그인 폼만 제공, 실제 처리는 시큐리티가...
    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

}