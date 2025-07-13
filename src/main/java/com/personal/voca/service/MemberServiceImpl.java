package com.personal.voca.service;

import com.personal.voca.dao.MemberDAO;
import com.personal.voca.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    // [1] ModelAndView 객체 생성
    private ModelAndView mav;

    // [2] DAO(Repository) 연결
    private final MemberDAO mdao;

    //세션 연결
    private final HttpSession session;

    private final HttpServletRequest request;

    private final JavaMailSender mailSender;

    // 암호화를 위한 객체
    private final PasswordEncoder pwEnc;

    private final VocaService vsvc;

    // 회원가입 메소드
    @Override
    public ModelAndView mJoin(MemberDTO member) throws IllegalStateException, IOException {

        mav = new ModelAndView();

        member.setMemPw(pwEnc.encode(member.getMemPw()));
        // (5) 입력
        int result = mdao.mJoin(member);

        // (6) 이동
        if (result > 0) {
            mav.setViewName("mLogin");
        } else {
            mav.setViewName("mJoin");
        }
        return mav;
    }

    // 회원목록 메소드
    public ModelAndView mList() {

        mav = new ModelAndView();

        List<MemberDTO> memberlist = mdao.mlist();

        // mav : Model And View
        // Model : Object
        // View : jsp

        if (!memberlist.isEmpty()) {
            mav.setViewName("M_List");
            mav.addObject("memberList", memberlist);
        } else {
            mav.setViewName("index");
        }
        return mav;
    }

    // 검색
    public ModelAndView mView(String memId) {

        mav = new ModelAndView();

        MemberDTO member = mdao.mView(memId);

        mav = vsvc.vocaTable(memId,1,1);

        mav.addObject("member", member);
        mav.setViewName("mView");

        return mav;
    }

    // 로그인
    public ModelAndView mLogin(MemberDTO member) {

        mav = new ModelAndView();

        MemberDTO user = mdao.mLogin(member);

        // (2) 로그인

        if(user != null && pwEnc.matches(member.getMemPw(), user.getMemPw())){
            mav.setViewName("home");
            session.setAttribute("userId", member.getMemId());
        }else{
            mav.setViewName("mLogin");
        }

        return mav;
    }


    // 회원삭제
    public ModelAndView mDelete(String mId) {

        mav = new ModelAndView();

        int result = mdao.mDelete(mId);

        if (result > 0) {
            mav.setViewName("redirect:mLogout");
        } else {
            mav.setViewName("mView");
        }
        return mav;
    }

    // 아이디 중복검사 + ajax
    public String idoverlap(String memId) {

        String result = mdao.mCheckID(memId);

        if (result == null) {
            // 아이디 사용가능
            return "OK";
        } else {
            // 이미 사용중인 아이디
            return "NO";
        }
    }

    public String mCheckEmail(String memEmail) {
        System.out.println("[2] 메일인증 service");
        String uuid = UUID.randomUUID().toString().substring(0, 6);

        //메일 보내기
        MimeMessage mail = mailSender.createMimeMessage();
        String mailContent = "<h2>안녕하세요. Voca 사이트 입니다 </h2><br/>"
                + "<h3>아래의 인증번호를 입력해주세요.</h3>"
                + "<h3>인증번호는 " + uuid + "입니다.</h3>";
        try {
            mail.setSubject("[Voca] 이메일 인증메세지", "UTF-8");
            mail.setText(mailContent, "UTF-8", "html");
            mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(memEmail));
            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();

        }
        System.out.println("메일 전송");
        return uuid;
    }





}
