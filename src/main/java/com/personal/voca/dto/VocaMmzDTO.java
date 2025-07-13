package com.personal.voca.dto;

import lombok.Data;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaMmz")
public class VocaMmzDTO {

    private int voNum;
    private String voTbName;
    private String voSp;
    private String voHw;
    private int voDirNum;
    
    private String partOne;
    private String partTwo;
    
    private int btnNum;
    private List<String> btnRan;
    
    
    private List<String> hwCons;
    private List<String> hwConsOne;
    private List<String> hwConsTwo;
    
    
    
    
    
    
    
   

}
