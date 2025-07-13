package com.personal.voca.dao;


import com.personal.voca.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberDAO {



    int mJoin(MemberDTO member);

    MemberDTO mLogin(MemberDTO member);

    List<MemberDTO> mlist();

    MemberDTO mView(String memId);

    int mModify(MemberDTO member);

    int mDelete(String mId);

    String mCheckID(String memId);



}
