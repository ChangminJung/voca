package com.personal.voca.service;


import com.personal.voca.dto.VocaDTO;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface VocaService {


    ModelAndView vocaSubmit(String tbName, String input);

    ModelAndView vocaTable();

    ModelAndView vocaView(int dirNum);

    ModelAndView vocaTest(int dirNum, int level, int type);

    ModelAndView vocaCard(int dirNum, int type);

    List<List<VocaDTO>> checkSave(String input, int dirNum);

    String imgSearch(String input);

    ModelAndView vocaModiForm(int dirNum);

    ModelAndView vocaModify(String tbName, int dirNum, String input);

    ModelAndView imgModiform(int voNum);

    List<Object> imgModify(int voNum);

    String imgPick(int voNum, int imgLv, String imgUrl);

    ModelAndView vocaCardQuiz(int dirNum, int quizLv, int type);

    ModelAndView quizResult(int dirNum, String input);

    String dirNumCheck();

    List<VocaDTO> searching(int dirNum);


    String randomVoca();

    ModelAndView vocaTenQuiz(int dirNum, int type);



}
