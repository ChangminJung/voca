package com.personal.voca.service;


import com.personal.voca.dto.MemberDTO;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public interface MemberService {

    // 아이디 중복체크 메소드
    ModelAndView mJoin(MemberDTO member) throws IOException;

    ModelAndView mList();

    ModelAndView mView(String memId);

    ModelAndView mLogin(MemberDTO member);

    ModelAndView mDelete(String memId);

    String idoverlap(String memId);

    String mCheckEmail(String memEmail);





}
