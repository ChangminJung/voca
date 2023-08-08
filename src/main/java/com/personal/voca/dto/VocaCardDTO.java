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

    // 퀴즈 결과에만 사용
    private String voInput;
    private int sum;

}
