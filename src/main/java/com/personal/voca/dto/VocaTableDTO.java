package com.personal.voca.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaTb")
public class VocaTableDTO {

    private int voDirNum;
    private String voTbName;
    private String voDate;


}
