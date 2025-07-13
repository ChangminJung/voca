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

    private String voPartOne;
    private String voPartTwo;
    
    private String voImg;
    private int voImgLv;

    private String voExample1;
    private String voExample2;
    private String voExample3;
    private String voExample4;
    private String voExample5;
    
    private int voCheck;

    private String ownerId;

    private int voShare;
    
    
    
    
    


}
