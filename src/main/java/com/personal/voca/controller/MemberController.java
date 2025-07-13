package com.personal.voca.controller;

import com.personal.voca.dto.MemberDTO;
import com.personal.voca.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    ModelAndView mav = new ModelAndView();

    private final HttpSession session;
    private final MemberService msvc;


    @GetMapping("/mLogin")
    public String vocaLogin(){
        return "mLogin";
    }

    @GetMapping("/mJoin")
    public String vocaJoin(){
        return "mJoin";
    }

    // idoverlap : 아이디중복체크
    @RequestMapping(value = "/idoverlap", method = RequestMethod.POST)
    public @ResponseBody String idoverlap(@RequestParam("memId") String memId) {
        String result = msvc.idoverlap(memId);
        System.out.println(memId);
        return result;
    }

    // mEmailCheck : 이메일인증
    @RequestMapping(value = "/mEmailCheck", method = RequestMethod.POST)
    public @ResponseBody String mEmailCheck(@RequestParam("memEmail") String memEmail) {
        System.out.println("[1] 메일인증 controller"+memEmail);
        String uuid = msvc.mCheckEmail(memEmail);
        System.out.println("[3] 메일인증 controller"+memEmail);
        return uuid;
    }

    // mJoin : 회원가입
    @RequestMapping(value = "/mJoin", method = RequestMethod.POST)
    public ModelAndView mJoin(@ModelAttribute MemberDTO member) throws IllegalStateException, IOException {

        mav = msvc.mJoin(member);
        return mav;
    }

    // mLogin : 로그인
    @RequestMapping(value = "/mLogin", method = RequestMethod.POST)
    public ModelAndView mLogin(@ModelAttribute MemberDTO member) {
        mav = msvc.mLogin(member);
        return mav;
    }

    @GetMapping(value = "mLogOut")
    public String mLogOut() {
        session.removeAttribute("userId");
        return "home";
    }


    //mView : 회원정보
    @GetMapping(value = "mView")
    public ModelAndView mView(@RequestParam("memId") String memId) {

        mav = msvc.mView(memId);

        return mav;
    }


}
