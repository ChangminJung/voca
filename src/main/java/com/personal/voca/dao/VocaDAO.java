package com.personal.voca.dao;


import com.personal.voca.dto.VocaCheckDTO;
import com.personal.voca.dto.VocaDTO;
import com.personal.voca.dto.VocaMmzDTO;
import com.personal.voca.dto.VocaRowDTO;
import com.personal.voca.dto.VocaStcDTO;
import com.personal.voca.dto.VocaTableDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VocaDAO {


    void vocaSubmit(VocaDTO voca);
    void voDirSubmit(VocaTableDTO vocaTb);

    List<VocaTableDTO> vocaTable(VocaTableDTO vocaTbDTO);
    List<VocaTableDTO> memTable(VocaTableDTO vocaTbDTO);
    
    String vocaUserId(int dirNum);
    
    String vocaCheckInput(VocaCheckDTO vocaCk);
    
    void checkSave(VocaCheckDTO vocaCk);
    
    void checkInsert(VocaCheckDTO vocaCk);

    int vocaCount(String userId);

    List<VocaDTO> vocaView(int dirNum);
    
    List<VocaStcDTO> vocaStcView(int dirNum);

    VocaDTO vocaViewOne(int voNum);

    List<VocaDTO> vocaPart(VocaDTO voca);
    
    void imgUpdate(VocaDTO voca);

    void vocaMod(VocaDTO voca);

    void vocaModImg(VocaDTO voca);

    void imgPick(VocaDTO voca);

    int searchCount(int dirNum);

    String dirNumCheck();

    List<VocaDTO> vocaSearch(VocaRowDTO vocaRN);

    void voDirModify(VocaDTO voca);

    int allCount();

    VocaDTO vocaRanOne(int ranNum);



}
