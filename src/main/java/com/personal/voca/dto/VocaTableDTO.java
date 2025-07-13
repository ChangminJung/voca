package com.personal.voca.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("vocaTb")
public class VocaTableDTO {

    private int voDirNum;
    private String voTbName;
    private String voDate;

    private String voOwnerId;
    private int voShare;
    private int rowN;
    private int startRow;
    private int endRow;

}
