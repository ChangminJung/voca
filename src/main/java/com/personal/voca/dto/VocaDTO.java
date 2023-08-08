package com.personal.voca.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("voca")
public class VocaDTO {

    private int voNum;
    private String voTbName;
    private String voSp;
    private String voHw;
    private String voDate;
    private int voDirNum;

    private String voImg;
    private int voImgLv;

    private int voCheck;



}
