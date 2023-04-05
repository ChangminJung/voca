package com.personal.voca.service;


import org.springframework.web.servlet.ModelAndView;

public interface VocaService {


    ModelAndView vocaSubmit(String tbName, String input);

    ModelAndView vocaTable();

    ModelAndView vocaView(int dirNum);

    ModelAndView vocaTest(int dirNum, int level);

    ModelAndView vocaCard(int dirNum);

    String imgSearch(String input);
}
