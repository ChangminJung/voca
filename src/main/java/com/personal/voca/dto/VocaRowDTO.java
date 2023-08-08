package com.personal.voca.dto;


import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaRN")
public class VocaRowDTO {
    private int voDirNum;
    private int startNum;
    private int endNum;
}
