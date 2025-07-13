package com.personal.voca.service;


import com.personal.voca.dto.VocaDTO;
import com.personal.voca.dto.VocaTableDTO;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface VocaService {


    ModelAndView vocaSubmit(String tbName, String input, String userId, int share);

    ModelAndView vocaTable(String userId , int page, int type);

    List<Object> vocaList(String userId, int page);

    ModelAndView vocaView(int dirNum);

    ModelAndView vocaTest(int dirNum, int level, int type);

    ModelAndView vocaCard(int dirNum, int type);

    List<List<VocaDTO>> checkSave(String input, int dirNum);

    String imgSearch(String input);

    ModelAndView vocaModiForm(int dirNum);

    ModelAndView vocaModify(String tbName, int dirNum, String input, int share);

    ModelAndView imgModiform(int voNum);

    List<Object> imgModify(int voNum);

    String imgPick(int voNum, int imgLv, String imgUrl);

    ModelAndView vocaCardQuiz(int dirNum, int quizLv, int type);

    ModelAndView vocaMemorize(int dirNum);
    
    ModelAndView vocaStcTraining(int dirNum);
    
    ModelAndView quizResult(int dirNum, String input);

    String dirNumCheck();

    List<VocaDTO> searching(int dirNum);


    String randomVoca();

    ModelAndView vocaTenQuiz(int dirNum, int type);



}
