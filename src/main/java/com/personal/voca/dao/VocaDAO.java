package com.personal.voca.dao;


import com.personal.voca.dto.VocaDTO;

import com.personal.voca.dto.VocaRowDTO;
import com.personal.voca.dto.VocaTableDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VocaDAO {


    void vocaSubmit(VocaDTO voca);
    void voDirSubmit(VocaDTO voca);

    List<VocaTableDTO> vocaTable();

    List<VocaDTO> vocaView(int dirNum);

    List<VocaDTO> vocaCheckView(int dirNum);

    VocaDTO vocaViewOne(int voNum);

    void checkSave(VocaDTO voca);

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
