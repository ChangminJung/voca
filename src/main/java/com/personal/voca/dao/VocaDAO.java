package com.personal.voca.dao;


import com.personal.voca.dto.VocaDTO;
import com.personal.voca.dto.VocaTableDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VocaDAO {


    void vocaSubmit(VocaDTO voca);
    void voDirSubmit(VocaDTO voca);

    List<VocaTableDTO> vocaTable();

    List<VocaDTO> vocaView(int dirNum);

    VocaDTO vocaViewOne(int voNum);

    void imgUpdate(VocaDTO voca);
}
