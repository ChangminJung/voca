package com.personal.voca.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaCk")
public class VocaCheckDTO {

    private int voDirNum;
    private String voUserId;
    private String voCheckInput;

    

}
