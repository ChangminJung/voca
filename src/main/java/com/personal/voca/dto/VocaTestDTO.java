package com.personal.voca.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaTest")
public class VocaTestDTO {

    private int voNum;
    private String voTbName;
    private String firstStr;
    private String lastStr;
    private String quizStr;
    private int quizLen;
    private String hw;
    private int voDirNum;
    private int curLevel;

//    private String voDate;

}
