package com.personal.voca.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

@Data
@Alias("member")
public class MemberDTO {

    private String memId;					// 아이디
    private String memPw;					// 비밀번호
    private String memEmail;				// 이메일


}

