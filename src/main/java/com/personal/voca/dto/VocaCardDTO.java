package com.personal.voca.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaCard")
public class VocaCardDTO {

    private int voNum;
    private String voTbName;
    private String voSp;
    private String voHw;
    private int voDirNum;

    private String voImg;

//    private String voDate;

}
