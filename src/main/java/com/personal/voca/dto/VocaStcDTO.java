package com.personal.voca.dto;

import lombok.Data;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaStc")
public class VocaStcDTO {

    private int voStcNum;
    private int voNum;  
    private int voDirNum;
    
    private String voSp;
    private String voHw;
    
    private String voStc;
    private String voStcMean;
    
    private String voStcHtml;
    private String voStcMnHtml;
    
    private String voWdMean;
    
    private String voPartOne;
    private String voPartTwo;
    
    private List<String> voWord;
    
    private List<String> voCons;
    
    
    
    
    
    
    
   

}
