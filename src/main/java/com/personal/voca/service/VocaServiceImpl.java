package com.personal.voca.service;

import com.personal.voca.dao.VocaDAO;
import com.personal.voca.dto.*;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.lang.invoke.SwitchPoint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocaServiceImpl implements VocaService {

    private ModelAndView mav;

    private final HttpSession session;

    private final VocaDAO vdao;

    @Override
    public ModelAndView vocaSubmit(String tbName, String input, String userId, int share) {

        mav = new ModelAndView();
        input = input.toLowerCase();
        VocaDTO voca = new VocaDTO();
        VocaTableDTO vocaTb = new VocaTableDTO();
        voca.setVoTbName(tbName);
        vocaTb.setVoTbName(tbName);
        LocalDateTime date = LocalDateTime.now();
        voca.setVoDate(date.toString().substring(0,10));
        vocaTb.setVoDate(date.toString().substring(0,10));
        vocaTb.setVoOwnerId(userId);
        vocaTb.setVoShare(share);
        vdao.voDirSubmit(vocaTb);

        /*
        String imgHtml = "";
        String imgCode = "";
        String imgSrc = "";
        String example1 = "";
        String example2 = "";
        String example3 = "";
        String example4 = "";
        String example5 = "";
        int codeIndex = 0;
        int codeIndex2 = 0;
        int codeIndex3 = 0; */
        int vocaBool = input.lastIndexOf("_");
        while (vocaBool != -1){
            int spIndex = 0;
            int hwIndex = 0;

            if(vocaBool != -1){
                spIndex = input.lastIndexOf('_',vocaBool-1);
                hwIndex = input.lastIndexOf('/',vocaBool);

                voca.setVoSp(input.substring(spIndex+1,hwIndex));
                voca.setVoHw(input.substring(hwIndex+1,vocaBool));

                /*
                try{    // 사진 크롤링
                    String gooCrl = "https://www.google.com/search?q=" + voca.getVoSp() +
                            "&tbm=isch&tbs=il:cl&hl=ko&sa=X&ved=0CAAQ1vwEahcKEwiA0ZmajeSEAxUAAAAAHQAAAAAQAw&biw=1691&bih=915";
                    Document doc = Jsoup.connect(gooCrl).get();
                    Elements eUrl = doc.select("div.isv-r");
                    imgCode = eUrl.get(1).attr("data-tbnid");

                    gooCrl = "https://www.google.com/search?q=" + voca.getVoSp()
                            + "&tbm=isch&tbs=il:cl&hl=ko&sa=X&ved=0CAAQ1vwEahcKEwiA0ZmajeSEAxUAAAAAHQAAAAAQAw&biw=1691&bih=915#imgrc=" + imgCode;

                    doc = Jsoup.connect(gooCrl).get();

                    eUrl = doc.select("script");
                    imgHtml = eUrl.toString();
                    codeIndex = imgHtml.indexOf(imgCode);
                    codeIndex2 = imgHtml.indexOf("]", codeIndex);
                    codeIndex3 = imgHtml.indexOf("\"", codeIndex2+4);

                    imgSrc = imgHtml.substring(codeIndex2+4,codeIndex3);
                    voca.setVoImg(imgSrc);

                    
                }catch (Exception e){
                    voca.setVoImg("");
                    
                }
                
                try {
                	String gooCrl2 = "https://news.google.com/search?q=intitle%3A" + voca.getVoSp() + "&hl=en-US&gl=US&ceid=US%3Aen";
                	
                    Document doc2 = Jsoup.connect(gooCrl2).get();
                        
                    Elements eUrl2 = doc2.select("a.JtKRv");
                    
                    example1 = eUrl2.get(0).html() + "*" + eUrl2.get(0).attr("href").substring(1, eUrl2.get(0).attr("href").length());
                    example2 = eUrl2.get(1).html() + "*" + eUrl2.get(1).attr("href").substring(1, eUrl2.get(1).attr("href").length());
                    example3 = eUrl2.get(2).html() + "*" + eUrl2.get(2).attr("href").substring(1, eUrl2.get(2).attr("href").length());
                    example4 = eUrl2.get(3).html() + "*" + eUrl2.get(3).attr("href").substring(1, eUrl2.get(3).attr("href").length());
                    example5 = eUrl2.get(4).html() + "*" + eUrl2.get(4).attr("href").substring(1, eUrl2.get(4).attr("href").length());
                    
                    voca.setVoExample1(example1);
                    voca.setVoExample2(example2);
                    voca.setVoExample3(example3);
                    voca.setVoExample4(example4);
                    voca.setVoExample5(example5);
                    

                    
//                  String gooCrl2 = "https://papago.naver.com/?sk=en&tk=ko&hn=1&st=";  
//                  Document doc2 = Jsoup.connect(gooCrl2).get();
                    
                    
				} catch (Exception e) {
					voca.setVoExample1("");
                    voca.setVoExample2("");
                    voca.setVoExample3("");
                    voca.setVoExample4("");
                    voca.setVoExample5("");
				} */

                vdao.vocaSubmit(voca);
            }
            vocaBool = input.lastIndexOf("_", vocaBool-1);

        }
        mav.setViewName("home");

        return mav;
    }

    @Override
    public ModelAndView vocaTable(String userId, int page, int share) {

        PageDTO paging = new PageDTO();
        paging.setPage(page);
        mav = new ModelAndView();

        int block = 5;
        // 한 화면에 보여줄 페이지 번호 개수

        paging.setLimit(10);
        // 한 화면에 보여줄 게시글 개수
        int limit = paging.getLimit();

        // 전체 게시글 개수
        int eqCount = vdao.vocaCount(userId);

        // ceil : (소숫점)올림
        int maxPage = (int) (Math.ceil((double) eqCount / limit));
        int startPage = (((int) (Math.ceil((double) paging.getPage() / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        if (startPage > maxPage) {
            startPage = maxPage;
        }

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        if (paging.getPage() > maxPage) {
            paging.setPage(maxPage);
        }

        int startRow = (paging.getPage() - 1) * limit + 1;
        int endRow = paging.getPage() * limit;

        PageDTO paging1 = new PageDTO();

        paging1.setPage(paging.getPage());
        paging1.setLimit(limit);
        paging1.setStartRow(startRow);
        paging1.setEndRow(endRow);
        paging1.setStartPage(startPage);
        paging1.setEndPage(endPage);
        paging1.setMaxPage(maxPage);
        mav.addObject("paging", paging1);

        VocaTableDTO vocaTbDTO = new VocaTableDTO();
        List<VocaTableDTO> vocaTable = new ArrayList<>();

        vocaTbDTO.setVoOwnerId(userId);
        vocaTbDTO.setStartRow(paging1.getStartRow());
        vocaTbDTO.setEndRow(paging1.getEndRow());

        
        if(share == 0) {	// 공유 테이블 포함
        	vocaTable = vdao.vocaTable(vocaTbDTO);	
        }else {	// 개인 테이블만
        	vocaTable = vdao.memTable(vocaTbDTO);
        }
        
        
        mav.setViewName("vocaTable");
        mav.addObject("vocaTable", vocaTable);

        return mav;
    }

    @Override
    public List<Object> vocaList(String userId, int page) {

        List<Object> data = new ArrayList<Object>();

        PageDTO paging = new PageDTO();
        paging.setPage(page);

        // 한 화면에 보여줄 페이지 번호 개수
        int block = 5;

        paging.setLimit(10);
        // 한 화면에 보여줄 게시글 개수
        int limit = paging.getLimit();

        // 전체 게시글 개수
        int eqCount = vdao.vocaCount(userId);

        // ceil : (소숫점)올림
        int maxPage = (int) (Math.ceil((double) eqCount / limit));
        int startPage = (((int) (Math.ceil((double) paging.getPage() / block))) - 1) * block + 1;
        int endPage = startPage + block - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        if (paging.getPage() > maxPage) {
            paging.setPage(maxPage);
        }

        int startRow = (paging.getPage() - 1) * limit + 1;
        int endRow = paging.getPage() * limit;

        PageDTO paging1 = new PageDTO();

        paging1.setPage(paging.getPage());
        paging1.setLimit(limit);
        paging1.setStartRow(startRow);
        paging1.setEndRow(endRow);
        paging1.setStartPage(startPage);
        paging1.setEndPage(endPage);
        paging1.setMaxPage(maxPage);

        VocaTableDTO vocaTbDTO = new VocaTableDTO();
        List<VocaTableDTO> vocaTable = new ArrayList<>();

        vocaTbDTO.setVoOwnerId(userId);
        vocaTbDTO.setStartRow(paging1.getStartRow());
        vocaTbDTO.setEndRow(paging1.getEndRow());

        vocaTable = vdao.vocaTable(vocaTbDTO);

        data.add(vocaTable);
        data.add(paging1);

        return data;
    }

    @Override
    public ModelAndView vocaView(int dirNum) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaDTO> vocaCheck = new ArrayList<>();
        
        String userId = String.valueOf(session.getAttribute("userId"));
        VocaCheckDTO vocaCk = new VocaCheckDTO();
        vocaCk.setVoDirNum(dirNum);
        vocaCk.setVoUserId(userId);
        vocaCk.setVoCheckInput(vdao.vocaCheckInput(vocaCk));
        String input = vocaCk.getVoCheckInput();
        
        if(vocaCk.getVoCheckInput() == null) {
        	voca = vdao.vocaView(dirNum);
        	vocaCheck = vdao.vocaView(dirNum);
        }else {
        	int vocaBool = 0;
            while (vocaBool < input.length()-1){
                VocaDTO voca0 = new VocaDTO();

                int checkIndex = 0;
                int voNumIndex = 0;

                if(vocaBool != -1){
                    checkIndex = input.indexOf('_',vocaBool+1);
                    voNumIndex = input.indexOf('/',vocaBool);

                    if(Integer.parseInt(input.substring(voNumIndex+1,checkIndex)) == 1) {
                    	voca0 = vdao.vocaViewOne(Integer.parseInt(input.substring(vocaBool+1,voNumIndex)));
                    	voca0.setVoCheck(1);
                    }else {
                    	voca0 = vdao.vocaViewOne(Integer.parseInt(input.substring(vocaBool+1,voNumIndex)));
                        
                        vocaCheck.add(voca0);
                    }
                    voca.add(voca0);
                }
                vocaBool = input.indexOf("_", vocaBool+1);
            }
        }
        for(int i = 0; i < vocaCheck.size(); i++) {
            vocaCheck.get(i).setVoHw(vocaCheck.get(i).getVoHw().replaceAll(",", ", "));
            vocaCheck.get(i).setVoHw(vocaCheck.get(i).getVoHw().replaceAll(";", "; "));
        }
        for(int i = 0; i < voca.size(); i++) {
            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(",", ", "));
            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(";", "; "));
        }
        
        
        String ownerId = vdao.vocaUserId(dirNum);
        mav.addObject("ownerId", ownerId);
        
        mav.setViewName("vocaView");
        mav.addObject("voca", voca);
        mav.addObject("vocaCheck", vocaCheck);
        System.out.println(vocaCheck);

        // 단어장 만들기용
        String input0 = "vocaSubmit?tbName=Day10&input=";
        for(int i = 0; i < voca.size(); i++){
            input0+= voca.get(i).getVoSp() + "/" + voca.get(i).getVoHw() + "_";
        }
//        System.out.println(input);


        return mav;
    }

    @Override
    public List<List<VocaDTO>> checkSave(String input, int dirNum) {

    	List<List<VocaDTO>> data = new ArrayList<>();
    	List<VocaDTO> vocaList = new ArrayList<>();
        List<VocaDTO> vocaCheck = new ArrayList<>();        
        
        String userId = String.valueOf(session.getAttribute("userId"));
        VocaCheckDTO vocaCk = new VocaCheckDTO();
        vocaCk.setVoDirNum(dirNum);
        vocaCk.setVoUserId(userId);
        vocaCk.setVoCheckInput(input);
        
        if(vdao.vocaCheckInput(vocaCk) == null) {
        	vdao.checkInsert(vocaCk);
        }else {
        	vdao.checkSave(vocaCk);	
        }
        
        int vocaBool = 0;
        while (vocaBool < input.length()-1){
            VocaDTO voca = new VocaDTO();

            int checkIndex = 0;
            int voNumIndex = 0;

            if(vocaBool != -1){
                checkIndex = input.indexOf('_',vocaBool+1);
                voNumIndex = input.indexOf('/',vocaBool);

                if(Integer.parseInt(input.substring(voNumIndex+1,checkIndex)) == 1) {
                	voca = vdao.vocaViewOne(Integer.parseInt(input.substring(vocaBool+1,voNumIndex)));
                	voca.setVoCheck(1);
                }else {
                	voca = vdao.vocaViewOne(Integer.parseInt(input.substring(vocaBool+1,voNumIndex)));
                    
                    vocaCheck.add(voca);
                }
                vocaList.add(voca);
            }
            vocaBool = input.indexOf("_", vocaBool+1);
        }
        for(int i = 0; i < vocaCheck.size(); i++) {
            vocaCheck.get(i).setVoHw(vocaCheck.get(i).getVoHw().replaceAll(",", ", "));
            vocaCheck.get(i).setVoHw(vocaCheck.get(i).getVoHw().replaceAll(";", "; "));
        }
        for(int i = 0; i < vocaList.size(); i++) {
        	vocaList.get(i).setVoHw(vocaList.get(i).getVoHw().replaceAll(",", ", "));
        	vocaList.get(i).setVoHw(vocaList.get(i).getVoHw().replaceAll(";", "; "));
        }
        
        data.add(vocaCheck);
        data.add(vocaList);
        
        return data;
    }

    @Override
    public ModelAndView vocaTest(int dirNum, int level, int type) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaTestDTO> vocaTestList = new ArrayList<>();

        if(type == 0){
            voca = vdao.vocaView(dirNum);
        }else{
        	String userId = String.valueOf(session.getAttribute("userId"));
            VocaCheckDTO vocaCk = new VocaCheckDTO();
            vocaCk.setVoDirNum(dirNum);
            vocaCk.setVoUserId(userId);
            vocaCk.setVoCheckInput(vdao.vocaCheckInput(vocaCk));
            String input = vocaCk.getVoCheckInput();
            
            if(vocaCk.getVoCheckInput() == null) {
            	voca = vdao.vocaView(dirNum);
            }else {
            	int vocaBool = 0;
                while (vocaBool < input.length()-1){
                    VocaDTO voca0 = new VocaDTO();

                    int checkIndex = 0;
                    int voNumIndex = 0;

                    if(vocaBool != -1){
                        checkIndex = input.indexOf('_',vocaBool+1);
                        voNumIndex = input.indexOf('/',vocaBool);

                        if(Integer.parseInt(input.substring(voNumIndex+1,checkIndex)) == 1) {
                        }else {
                        	voca0 = vdao.vocaViewOne(Integer.parseInt(input.substring(vocaBool+1,voNumIndex)));
                            
                            voca.add(voca0);
                        }
                    }
                    vocaBool = input.indexOf("_", vocaBool+1);
                }
            }
        }
        String ranStr = "";
        int ranCount = 0;
        for( int i = (int) Math.floor(Math.random() * voca.size()) ; ranCount < voca.size() ; ){

            VocaTestDTO vocaTest = new VocaTestDTO();

            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(",",", "));
            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(";","; "));

            int quizLen = voca.get(i).getVoSp().length()/(4-level);
            int quizIndex = (int) Math.floor(Math.random()* (voca.get(i).getVoSp().length()- quizLen +1));

            String firstStr = (quizIndex == 0) ? "" : voca.get(i).getVoSp().substring(0,quizIndex);
            String lastStr = (quizIndex == voca.get(i).getVoSp().length()-quizLen) ? "" : voca.get(i).getVoSp().substring(quizIndex+quizLen, voca.get(i).getVoSp().length());
            String quizStr = voca.get(i).getVoSp().substring(quizIndex,quizIndex+quizLen);

            vocaTest.setVoTbName(voca.get(i).getVoTbName());
            vocaTest.setVoNum(voca.get(i).getVoNum());
            vocaTest.setFirstStr(firstStr);
            vocaTest.setLastStr(lastStr);
            vocaTest.setQuizStr(quizStr);
            vocaTest.setQuizLen(quizLen);
            vocaTest.setHw(voca.get(i).getVoHw());
            vocaTest.setVoDirNum(dirNum);
            vocaTest.setCurLevel(level);

            // random 작업 ------
            // i = 0 ~ size-1 까지
            ranStr += " "+ i + " ";
            ranCount++;
            while( ranStr.indexOf(" "+ i + " ",0) != -1 && ranCount < voca.size()){
                i = (int) Math.floor(Math.random() * voca.size());
            }
            // -----------------
            System.out.println(i);
            System.out.println(ranCount);
            vocaTestList.add(vocaTest);
            System.out.println(vocaTest);
        }
        mav.setViewName("vocaTest");
        mav.addObject("vocaTestList", vocaTestList);
        mav.addObject("vocaType", type);

        return mav;
    }

    @Override
    public ModelAndView vocaCard(int dirNum, int type) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaCardDTO> vocaCardList = new ArrayList<>();

        if(type == 0){
            voca = vdao.vocaView(dirNum);
        }else{
        	String userId = String.valueOf(session.getAttribute("userId"));
            VocaCheckDTO vocaCk = new VocaCheckDTO();
            vocaCk.setVoDirNum(dirNum);
            vocaCk.setVoUserId(userId);
            vocaCk.setVoCheckInput(vdao.vocaCheckInput(vocaCk));
            String input = vocaCk.getVoCheckInput();
            
            if(vocaCk.getVoCheckInput() == null) {
            	voca = vdao.vocaView(dirNum);
            }else {
            	int vocaBool = 0;
                while (vocaBool < input.length()-1){
                    VocaDTO voca0 = new VocaDTO();

                    int checkIndex = 0;
                    int voNumIndex = 0;

                    if(vocaBool != -1){
                        checkIndex = input.indexOf('_',vocaBool+1);
                        voNumIndex = input.indexOf('/',vocaBool);

                        if(Integer.parseInt(input.substring(voNumIndex+1,checkIndex)) == 1) {
                        }else {
                        	voca0 = vdao.vocaViewOne(Integer.parseInt(input.substring(vocaBool+1,voNumIndex)));
                            
                            voca.add(voca0);
                        }
                    }
                    vocaBool = input.indexOf("_", vocaBool+1);
                }
            }
        }

        String ranStr = "";
        int ranCount = 0;
        for( int i = (int) Math.floor(Math.random() * voca.size()) ; ranCount < voca.size() ; ){

            VocaCardDTO vocaCard = new VocaCardDTO();

            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(",",", "));
            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(";","; "));

            vocaCard.setVoTbName(voca.get(i).getVoTbName());
            vocaCard.setVoNum(voca.get(i).getVoNum());

            vocaCard.setVoSp(voca.get(i).getVoSp());
            vocaCard.setVoHw(voca.get(i).getVoHw());

            vocaCard.setVoDirNum(dirNum);
            vocaCard.setVoImg(voca.get(i).getVoImg());

            // random 작업 ------
            // i = 0 ~ size-1 까지
            ranStr += " "+ i + " ";
            ranCount++;
            while( ranStr.indexOf(" "+ i + " ",0) != -1 && ranCount < voca.size()){
                i = (int) Math.floor(Math.random() * voca.size());
            }
            // -----------------
            vocaCardList.add(vocaCard);
            System.out.println(vocaCard);
        }

        String ownerId = vdao.vocaUserId(dirNum);
        mav.addObject("ownerId", ownerId);
        
        mav.setViewName("vocaCard");
        mav.addObject("vocaCardList", vocaCardList);
        mav.addObject("type", type);


        return mav;
    }

    @Override
    public String imgSearch(String input) {

        List<String> imgList = new ArrayList<>();

        if(input != null && input != ""){
        	System.out.println(input);
            input = input.substring(0,input.length()-1);
            imgList = List.of(input.split(" "));
            System.out.println(imgList);

            String imgHtml = "";
            String imgCode = "";
            String imgSrc = "";
            int codeIndex = 0;
            int codeIndex2 = 0;
            int codeIndex3 = 0;

            for (int i = 0; i < imgList.size(); i++){

                VocaDTO voca = new VocaDTO();

                voca = vdao.vocaViewOne(Integer.parseInt(imgList.get(i)));
                int imgLevel = voca.getVoImgLv();
                imgLevel++;

                try{    // 사진 크롤링
                	System.out.println(voca.getVoSp());
                	String gooCrl = "https://www.google.com/search?q=" + voca.getVoSp() +
                            "&tbm=isch&tbs=il:cl&hl=ko&sa=X&ved=0CAAQ1vwEahcKEwiA0ZmajeSEAxUAAAAAHQAAAAAQAw&biw=1691&bih=915";
                    Document doc = Jsoup.connect(gooCrl).get();
                    Elements eUrl = doc.select("div.isv-r");
                	
                    System.out.println(gooCrl);
                    System.out.println(imgLevel);
                    
                    System.out.println(eUrl.get(0));
                    System.out.println(eUrl.get(1));
                    System.out.println(eUrl.get(2));
                    System.out.println(eUrl.get(3));
                    System.out.println(eUrl.get(4));
                    
                    imgCode = eUrl.get(imgLevel).attr("data-tbnid");
                    System.out.println("imgCd : "+imgCode);
                    System.out.println(imgCode);
                    while (imgCode == null || imgCode == "") {
                        imgLevel++;
                        imgCode = eUrl.get(imgLevel).attr("data-tbnid");
                        System.out.println("imgCd : "+imgCode);
                    }
                    System.out.println(imgCode);
                    gooCrl = "https://www.google.com/search?q=" + voca.getVoSp()
                            + "&tbm=isch&tbs=il:cl&hl=ko&sa=X&ved=0CAAQ1vwEahcKEwiA0ZmajeSEAxUAAAAAHQAAAAAQAw&biw=1691&bih=915#imgrc=" + imgCode;
                    
                    doc = Jsoup.connect(gooCrl).get();

                    System.out.println("imgUrl : " + gooCrl);
                    System.out.println("imgLevel : " + imgLevel);

                    eUrl = doc.select("script");
                    imgHtml = eUrl.toString();
                    
                    codeIndex = imgHtml.indexOf(imgCode);
                    codeIndex2 = imgHtml.indexOf("]", codeIndex);
                    codeIndex3 = imgHtml.indexOf("\"", codeIndex2+4);

                    imgSrc = imgHtml.substring(codeIndex2+4,codeIndex3);

                    System.out.println("imgSrc : " + imgSrc);

                    voca.setVoImg(imgSrc);

                    voca.setVoImgLv(imgLevel);
                    vdao.imgUpdate(voca);

                }catch (Exception e){
                    return "이미지 크롤링 실패";
                }
            }
            return "검색 완료 \n 이미지를 보시려면 새로고침을 해주세요.";
        }else{
            return "검색할 이미지가 없습니다.";
        }
    }

    @Override
    public ModelAndView vocaModiForm(int dirNum) {

        mav = new ModelAndView();
        List<VocaDTO> voca = new ArrayList<>();
        voca = vdao.vocaView(dirNum);

        mav.setViewName("vocaModify");
        mav.addObject("voca", voca);

        return mav;
    }

    @Override
    public ModelAndView vocaModify(String tbName, int dirNum, String input, int share) {

        mav = new ModelAndView();

        VocaDTO voca = new VocaDTO();
        voca.setVoDirNum(dirNum);
        voca.setVoTbName(tbName);
        voca.setVoShare(share);
        vdao.voDirModify(voca);

        // spelling 다르게 입력될경우 img 데이터 지우기 ${} #{} 사용
        
        int vocaBool = input.lastIndexOf("_");
        while (vocaBool != -1){
            int spIndex = 0;
            int hwIndex = 0;
            int spModIndex = 0;
            int voNumIndex = 0;
            int spModBool = 0;  // 0이 안바뀐거, 1은 바뀐것 (이미지 지워주기)

            if(vocaBool != -1){
                spIndex = input.lastIndexOf('_',vocaBool-1);
                hwIndex = input.lastIndexOf('/',vocaBool);
                spModIndex = input.lastIndexOf('@',vocaBool);
                voNumIndex = input.lastIndexOf('*',vocaBool);

                voca.setVoNum(Integer.parseInt(input.substring(spIndex+1,voNumIndex)));
                spModBool = Integer.parseInt(input.substring(voNumIndex+1,spModIndex));
                voca.setVoSp(input.substring(spModIndex+1,hwIndex));
                voca.setVoHw(input.substring(hwIndex+1,vocaBool));

                if(spModBool == 0){
                    vdao.vocaMod(voca);
                }else{
                    vdao.vocaModImg(voca);
                }

            }
            vocaBool = input.lastIndexOf("_", vocaBool-1);

        }
        mav.setViewName("redirect:/vocaView?dirNum="+dirNum);

        return mav;
    }

    @Override
    public ModelAndView imgModiform(int voNum) {

        mav = new ModelAndView();

        mav.addObject("voNum", voNum);
        mav.setViewName("cardPopup");

        return mav;
    }

    @Override
    public List<Object> imgModify(int voNum) {

        mav = new ModelAndView();

        List<Object> imgModList = new ArrayList<>();

        VocaDTO voca = new VocaDTO();

        voca = vdao.vocaViewOne(voNum);
        int imgLevel = voca.getVoImgLv();
        imgLevel++;
        String[] imgCode = new String[5];
        String imgHtml = "";
        String imgSrc = "";
        int codeIndex = 0;
        int codeIndex2 = 0;
        int codeIndex3 = 0;

        List<Integer> imgLv = new ArrayList<>();
        List<String> imgUrl = new ArrayList<>();

        try{    // 사진 크롤링
            String gooCrl = "https://www.google.com/search?q=" + voca.getVoSp() +
                    "&tbm=isch&tbs=il:cl&hl=ko&sa=X&ved=0CAAQ1vwEahcKEwiA0ZmajeSEAxUAAAAAHQAAAAAQAw&biw=1691&bih=915";
            Document doc = Jsoup.connect(gooCrl).get();
            Elements eUrl = doc.select("div.isv-r");
            
            for(int i = imgLevel; i < imgLevel+5 ; i++){
                imgCode[i-imgLevel] = eUrl.get(i).attr("data-tbnid");

                while (imgCode[i-imgLevel] == null || imgCode[i-imgLevel] == "") {
                    imgLevel++;
                    i++;
                    imgCode[i-imgLevel] = eUrl.get(i).attr("data-tbnid");
                }
                imgLv.add(imgLevel);
            }


            for(int i = 0; i < 5; i ++){
                gooCrl = "https://www.google.com/search?q=" + voca.getVoSp()
                        + "&tbm=isch&tbs=il:cl&hl=ko&sa=X&ved=0CAAQ1vwEahcKEwiA0ZmajeSEAxUAAAAAHQAAAAAQAw&biw=1691&bih=915#imgrc=" + imgCode[i];

                doc = Jsoup.connect(gooCrl).get();

                System.out.println("imgUrl : " + gooCrl);
                System.out.println("imgLevel : " + imgLevel);

                eUrl = doc.select("script");
                imgHtml = eUrl.toString();

                codeIndex = imgHtml.indexOf(imgCode[i]);
                codeIndex2 = imgHtml.indexOf("]", codeIndex);
                codeIndex3 = imgHtml.indexOf("\"", codeIndex2+4);

                imgSrc = imgHtml.substring(codeIndex2+4,codeIndex3);

                imgUrl.add(imgSrc);
            }


        }catch (Exception e){
            return null;
        }

        System.out.println(voca);

        imgModList.add(voca);
        imgModList.add(imgLv);
        imgModList.add(imgUrl);

        return imgModList;
    }

    @Override
    public String imgPick(int voNum, int imgLv, String imgUrl) {

        System.out.println(imgUrl);
        VocaDTO voca = new VocaDTO();
        voca.setVoNum(voNum);
        voca.setVoImgLv(imgLv);
        voca.setVoImg(imgUrl);

        vdao.imgPick(voca);

        return "이미지가 선택되었습니다. \n새로고침 후에 확인할 수 있습니다.";
    }

    @Override
    public ModelAndView vocaCardQuiz(int dirNum, int quizLv, int type) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaCardDTO> vocaCardList = new ArrayList<>();
        List<VocaCardDTO> vocaQuizList = new ArrayList<>();

        if(type == 0){
            voca = vdao.vocaView(dirNum);
        }else{
        	String userId = String.valueOf(session.getAttribute("userId"));
            VocaCheckDTO vocaCk = new VocaCheckDTO();
            vocaCk.setVoDirNum(dirNum);
            vocaCk.setVoUserId(userId);
            vocaCk.setVoCheckInput(vdao.vocaCheckInput(vocaCk));
            String input = vocaCk.getVoCheckInput();
            
            if(vocaCk.getVoCheckInput() == null) {
            	voca = vdao.vocaView(dirNum);
            }else {
            	int vocaBool = 0;
                while (vocaBool < input.length()-1){
                    VocaDTO voca0 = new VocaDTO();

                    int checkIndex = 0;
                    int voNumIndex = 0;

                    if(vocaBool != -1){
                        checkIndex = input.indexOf('_',vocaBool+1);
                        voNumIndex = input.indexOf('/',vocaBool);

                        if(Integer.parseInt(input.substring(voNumIndex+1,checkIndex)) == 1) {
                        }else {
                        	voca0 = vdao.vocaViewOne(Integer.parseInt(input.substring(vocaBool+1,voNumIndex)));
                            
                            voca.add(voca0);
                        }
                    }
                    vocaBool = input.indexOf("_", vocaBool+1);
                }
            }
        }
        
       

        String ranStr = "";
        int ranCount = 0;
        for( int i = (int) Math.floor(Math.random() * voca.size()) ; ranCount < voca.size() ; ){

            VocaCardDTO vocaCard = new VocaCardDTO();

            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(",",", "));
            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(";","; "));

            vocaCard.setVoTbName(voca.get(i).getVoTbName());
            vocaCard.setVoNum(voca.get(i).getVoNum());

            vocaCard.setVoSp(voca.get(i).getVoSp());
            vocaCard.setVoHw(voca.get(i).getVoHw());

            vocaCard.setVoDirNum(dirNum);
            vocaCard.setVoImg(voca.get(i).getVoImg());

            // random 작업 ------
            // i = 0 ~ size-1 까지
            ranStr += " "+ i + " ";
            ranCount++;
            while( ranStr.indexOf(" "+ i + " ",0) != -1 && ranCount < voca.size()){
                i = (int) Math.floor(Math.random() * voca.size());
            }
            // -----------------
            vocaCardList.add(vocaCard);
            System.out.println(vocaCard);
        }

        for(int i = 0 ; i < vocaCardList.size()/quizLv ; i ++){
            vocaQuizList.add(vocaCardList.get(i));
        }

        mav.addObject("vocaCardList", vocaQuizList);

        mav.setViewName("vocaCardQuiz");

        return mav;
    }
    
    @Override
	public ModelAndView vocaStcTraining(int dirNum) {

		mav = new ModelAndView();

		List<VocaStcDTO> vocaStc = new ArrayList<>();

		vocaStc = vdao.vocaStcView(dirNum);

		System.out.println(vocaStc);

		for (int i = 0; i < vocaStc.size(); i++) {

			List<String> vocaWord = new ArrayList<>();
			String voWord = vocaStc.get(i).getVoWdMean();
			vocaWord = Arrays.asList(voWord.split("[*]"));
			vocaStc.get(i).setVoWord(vocaWord);

			// 한글 뜻 정리하는 작업 필요
			// (),'~' 아예 제거, ' ' 그대로 표현(+정답에는 영향없게) , ',' 으로 구분
			List<String> vocaCons = new ArrayList<>();

			String vocaHw = vocaStc.get(i).getVoHw();

			// 괄호 없애기 반복문
			while (vocaHw.indexOf("(") != -1 && vocaHw.indexOf(")") != -1) {
				vocaHw = vocaHw.replace(vocaHw.substring(vocaHw.indexOf("("), vocaHw.indexOf(")") + 1), "");
			}
			vocaHw = vocaHw.replaceAll("~", "");
			vocaHw = vocaHw.replaceAll(";", ",");

			if (vocaHw.indexOf(",") == -1) {
				vocaCons.add(vocaHw);
			} else {
				vocaCons = Arrays.asList(vocaHw.split(","));
			}

			List<String> vocaConsOne = new ArrayList<>();
			

			for (int k = 0; k < vocaCons.size(); k++) {
				// 맨 앞뒤 공백 지우기
				while (vocaCons.get(k).charAt(0) == ' ') {
					vocaCons.set(k, vocaCons.get(k).substring(1, vocaCons.get(k).length()));
				}
				while (vocaCons.get(k).charAt(vocaCons.get(k).length() - 1) == ' ') {
					vocaCons.set(k, vocaCons.get(k).substring(0, vocaCons.get(k).length() - 1));
				}

				String consOne = "";

				String lastThree = "";
				String lastTwo = "";
				String lastOne = "";

				// 맨 뒤 글자 추출
				if (vocaCons.get(k).length() >= 4) {
					lastThree = vocaCons.get(k).substring(vocaCons.get(k).length() - 3, vocaCons.get(k).length());
				}

				if (vocaCons.get(k).length() >= 3) {
					lastTwo = vocaCons.get(k).substring(vocaCons.get(k).length() - 2, vocaCons.get(k).length());
				}

				if (vocaCons.get(k).length() >= 2) {
					lastOne = vocaCons.get(k).substring(vocaCons.get(k).length() - 1, vocaCons.get(k).length());
				}

				// 하나만 고려
				if (vocaStc.get(i).getVoPartTwo() == null) {

					// 동사일 때
					if (vocaStc.get(i).getVoPartOne().equals("v")) {

						switch (lastTwo) {
						case "하다":
						case "주다":
						case "이다":
						case "보다":
						case "되다":

							
							consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-2);

							

							break;
						default:
							switch (lastOne) {
							case "다":

								consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-1);

								break;
							default:

								consOne = vocaCons.get(k);

								break;
							}

							break;
						}

						vocaConsOne.add(consOne);

					} else if (vocaStc.get(i).getVoPartOne().equals("adj")) {

						switch (lastThree) {
						case "으로는":

							consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-3);

							break;

						default:

							switch (lastTwo) {
							case "로는":
							case "되는":
							case "있는":
							case "하는":
							case "드는":
							case "같은":
							case "적인":

								consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-2);

								break;
							default:
								switch (lastOne) {
								case "는":
								case "은":
								case "의":
								case "한":
								case "인":
								case "된":
								case "운":

									consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-1);

									break;
								default:

									consOne = vocaCons.get(k);

									break;
								}

								break;
							}

							vocaConsOne.add(consOne);

							break;
						}

					} else if (vocaStc.get(i).getVoPartOne().equals("adv")) {

						switch (lastTwo) {
						case "으로":
						case "하게":

							consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-2);

							break;
						default:
							switch (lastOne) {
							case "히":
							case "로":
							case "게":
							case "에":

								consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-1);

								break;
							default:

								consOne = vocaCons.get(k);

								break;
							}

							break;
						}

						vocaConsOne.add(consOne);

					} else {

						consOne = vocaCons.get(k);
					}

				} else {
					// 두개 다 고려

					// 동사일 때
					if (vocaStc.get(i).getVoPartOne().equals("v") || vocaStc.get(i).getVoPartTwo().equals("v")) {

						switch (lastTwo) {
						case "하다":
						case "주다":
						case "이다":
						case "보다":
						case "되다":

							consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-2);

							break;
						default:
							switch (lastOne) {
							case "다":

								consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-1);

								break;
							default:

								break;
							}

							break;
						}

					}

					if (consOne == ""
							&& (vocaStc.get(i).getVoPartOne().equals("adj") || vocaStc.get(i).getVoPartTwo().equals("adj"))) {

						switch (lastThree) {
						case "으로는":

							consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-3);

							break;

						default:

							switch (lastTwo) {
							case "로는":
							case "되는":
							case "있는":
							case "하는":
							case "드는":
							case "같은":
							case "적인":

								consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-2);

								break;
							default:
								switch (lastOne) {
								case "는":
								case "은":
								case "의":
								case "한":
								case "인":
								case "된":
								case "운":

									consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-1);

									break;
								default:

									break;
								}

								break;
							}

							break;
						}

					}

					if (consOne == ""
							&& (vocaStc.get(i).getVoPartOne().equals("adv") || vocaStc.get(i).getVoPartTwo().equals("adv"))) {

						switch (lastTwo) {
						case "으로":
						case "하게":

							consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-2);

							break;
						default:
							switch (lastOne) {
							case "히":
							case "로":
							case "게":
							case "에":

								consOne = vocaCons.get(k).substring(0,vocaCons.get(k).length()-1);

								break;
							default:

								break;
							}

							break;
						}

					}

					// 동사, 형용사, 부사에 해당 안될 때 (-> 명사 등..)
					if (consOne == "") {

						consOne = vocaCons.get(k);

					}

					vocaConsOne.add(consOne);

				}
				
				if(!consOne.equals(vocaCons.get(k))) {
					vocaConsOne.add(vocaCons.get(k));
				}
				
			}
			// vacaCons에 정리 완료
			// (vocaHw 정리한것)
			
			// vocaConsOne 정리 (vocaHw+, keyword+)
			
			
			vocaStc.get(i).setVoCons(vocaConsOne);
			
			
			
		}

		
		mav.addObject("vocaStcList", vocaStc);
		mav.setViewName("stcTraining");
		
    	
    	return mav;
    }
    
	@Override
	public ModelAndView vocaMemorize(int dirNum) {
		
		mav = new ModelAndView();

		List<VocaDTO> voca = new ArrayList<>();

		voca = vdao.vocaView(dirNum);
		
		List<VocaMmzDTO> vocaMmzList = new ArrayList<>();
		
		for(int i = 0; i < voca.size(); i++) {
			
			List<VocaDTO> vocaPart = new ArrayList<>();
			
			vocaPart = vdao.vocaPart(voca.get(i));
			
			List<String> vocaBtn = new ArrayList<>();
			
			String ranStr = "";
	        int ranCount = 0;
	        for( int j = (int) Math.floor(Math.random() * vocaPart.size()) ; ranCount < 3 ; ){

	        	vocaBtn.add(vocaPart.get(j).getVoHw());
	        	
	            // random 작업 ------
	            // i = 0 ~ size-1 까지
	            ranStr += " "+ j + " ";
	            ranCount++;
	            while( ranStr.indexOf(" "+ j + " ",0) != -1 && ranCount < 3){
	                j = (int) Math.floor(Math.random() * vocaPart.size());
	            }
	        }
	        
	        
			VocaMmzDTO vocaMmz = new VocaMmzDTO(); 
	        
			vocaMmz.setVoNum(voca.get(i).getVoNum());
			vocaMmz.setVoSp(voca.get(i).getVoSp());
			vocaMmz.setVoHw(voca.get(i).getVoHw());
			vocaMmz.setVoTbName(voca.get(i).getVoTbName());
			vocaMmz.setVoDirNum(voca.get(i).getVoDirNum());
	    	vocaMmz.setBtnRan(vocaBtn);
	    	vocaMmz.setBtnNum((int) Math.floor(Math.random() * 4));
	    	
	    	// 한글 뜻 정리하는 작업 필요
	    	// () 아예 제거, '~', ' ' 그대로 표현(+정답에는 영향없게) , ',', ';" 으로 구분
	    	List<String> vocaCons = new ArrayList<>();
	    	
	    	String vocaHw = voca.get(i).getVoHw();
	    	
	    	
	    	// 괄호 없애기 반복문
	    	while(vocaHw.indexOf("(") != -1 && vocaHw.indexOf(")") != -1) {
	    		vocaHw = vocaHw.replace(vocaHw.substring(vocaHw.indexOf("("), vocaHw.indexOf(")")+1), "");
	    	}
	    	vocaHw = vocaHw.replaceAll(";", ",");
	    	
	    	if(vocaHw.indexOf(",") == -1) {
	    		vocaCons.add(vocaHw);
	    	}else {
	    		vocaCons = Arrays.asList(vocaHw.split(","));	
	    	}
	    	
	    	List<String> vocaConsOne = new ArrayList<>();
	    	List<String> vocaConsTwo = new ArrayList<>();

	    	for(int k = 0; k < vocaCons.size(); k++) {
		    	// 맨 앞뒤 공백 지우기	    		    		
	    		while(vocaCons.get(k).charAt(0) == ' ') {
	    			vocaCons.set(k, vocaCons.get(k).substring(1, vocaCons.get(k).length()));
		    	}
	    		while(vocaCons.get(k).charAt(vocaCons.get(k).length()-1) == ' ') {
	    			vocaCons.set(k, vocaCons.get(k).substring(0, vocaCons.get(k).length()-1));
		    	}
	    		
	    		String consOne = "";
		    	String consTwo = "";
		    	
		    	
	    		String lastThree = "";
	    		String lastTwo = "";
	    		String lastOne = "";
	    		
	    		// 맨 뒤 글자 추출
	    		if(vocaCons.get(k).length() >= 4) {
	    			lastThree = vocaCons.get(k).substring(vocaCons.get(k).length()-3 , vocaCons.get(k).length());
	    		}
	    		
	    		if(vocaCons.get(k).length() >= 3) {
	    			lastTwo = vocaCons.get(k).substring(vocaCons.get(k).length()-2 , vocaCons.get(k).length());
	    		}
	    		
	    		if(vocaCons.get(k).length() >= 2) {
	    			lastOne = vocaCons.get(k).substring(vocaCons.get(k).length()-1 , vocaCons.get(k).length());
	    		}

	    		// 하나만 고려
	    		if(voca.get(i).getVoPartTwo() == null) {

		    		// 동사일 때
		    		if(voca.get(i).getVoPartOne().equals("v")) {
		    			
		    			
			    		switch (lastTwo) {
						case "하다" : case "주다" : case "이다" : case "보다" :
						case "되다" :
							
							for(int w = 0; w < vocaCons.get(k).length()-2; w++) {
								char consChar = vocaCons.get(k).charAt(w);
								int consInt = (int)consChar;
								
								if(consInt >= 44032 && consInt < 44620) {
									// ㄱ
									consOne += "ㄱ";
									consTwo += "○";
								}else if(consInt >= 44620 && consInt < 45208) {
									// ㄲ
									consOne += "ㄲ";
									consTwo += "○";
								}else if(consInt >= 45208 && consInt < 45796) {
									// ㄴ
									consOne += "ㄴ";
									consTwo += "○";
								}else if(consInt >= 45796 && consInt < 46384) {
									// ㄷ
									consOne += "ㄷ";
									consTwo += "○";
								}else if(consInt >= 46384 && consInt < 46972) {
									// ㄸ
									consOne += "ㄸ";
									consTwo += "○";
								}else if(consInt >= 46972 && consInt < 47560) {
									// ㄹ
									consOne += "ㄹ";
									consTwo += "○";
								}else if(consInt >= 47560 && consInt < 48148) {
									// ㅁ
									consOne += "ㅁ";
									consTwo += "○";
								}else if(consInt >= 48148 && consInt < 48736) {
									// ㅂ
									consOne += "ㅂ";
									consTwo += "○";
								}else if(consInt >= 48736 && consInt < 49324) {
									// ㅃ
									consOne += "ㅃ";
									consTwo += "○";
								}else if(consInt >= 49324 && consInt < 49912) {
									// ㅅ
									consOne += "ㅅ";
									consTwo += "○";
								}else if(consInt >= 49912 && consInt < 50500) {
									// ㅆ
									consOne += "ㅆ";
									consTwo += "○";
								}else if(consInt >= 50500 && consInt < 51088) {
									// ㅇ
									consOne += "ㅇ";
									consTwo += "○";
								}else if(consInt >= 51088 && consInt < 51676) {
									// ㅈ
									consOne += "ㅈ";
									consTwo += "○";
								}else if(consInt >= 51676 && consInt < 52264) {
									// ㅉ
									consOne += "ㅉ";
									consTwo += "○";
								}else if(consInt >= 52264 && consInt < 52852) {
									// ㅊ
									consOne += "ㅊ";
									consTwo += "○";
								}else if(consInt >= 52852 && consInt < 53440) {
									// ㅋ
									consOne += "ㅋ";
									consTwo += "○";
								}else if(consInt >= 53440 && consInt < 54028) {
									// ㅌ
									consOne += "ㅌ";
									consTwo += "○";
								}else if(consInt >= 54028 && consInt < 54616) {
									// ㅍ
									consOne += "ㅍ";
									consTwo += "○";
								}else if(consInt >= 54616 && consInt < 55203) {
									// ㅎ
									consOne += "ㅎ";
									consTwo += "○";
								}else {
									// 한글이 아닌 문자 ('~', ' ')
									// 그대로 표현
									consOne += consChar;
									consTwo += consChar;
									
								}
								
								
							}
							
							consOne += lastTwo;
							consTwo += lastTwo;
							
							break;
						default:
							switch (lastOne) {
							case "다": 
								
								for(int w = 0; w < vocaCons.get(k).length()-1; w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
									
								}
								
								consOne += lastOne;
								consTwo += lastOne;
								
								break;
							default:
								
								for(int w = 0; w < vocaCons.get(k).length(); w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
								}
								
								break;
							}
							
							break;
						}
			    		
			    		vocaConsOne.add(consOne);
			    		vocaConsTwo.add(consTwo);
			    		
			    	}else if(voca.get(i).getVoPartOne().equals("adj")) {
			    		
			    		switch (lastThree) {
						case "으로는":
							
							for(int w = 0; w < vocaCons.get(k).length()-3; w++) {
								char consChar = vocaCons.get(k).charAt(w);
								int consInt = (int)consChar;
								
								if(consInt >= 44032 && consInt < 44620) {
									// ㄱ
									consOne += "ㄱ";
									consTwo += "○";
								}else if(consInt >= 44620 && consInt < 45208) {
									// ㄲ
									consOne += "ㄲ";
									consTwo += "○";
								}else if(consInt >= 45208 && consInt < 45796) {
									// ㄴ
									consOne += "ㄴ";
									consTwo += "○";
								}else if(consInt >= 45796 && consInt < 46384) {
									// ㄷ
									consOne += "ㄷ";
									consTwo += "○";
								}else if(consInt >= 46384 && consInt < 46972) {
									// ㄸ
									consOne += "ㄸ";
									consTwo += "○";
								}else if(consInt >= 46972 && consInt < 47560) {
									// ㄹ
									consOne += "ㄹ";
									consTwo += "○";
								}else if(consInt >= 47560 && consInt < 48148) {
									// ㅁ
									consOne += "ㅁ";
									consTwo += "○";
								}else if(consInt >= 48148 && consInt < 48736) {
									// ㅂ
									consOne += "ㅂ";
									consTwo += "○";
								}else if(consInt >= 48736 && consInt < 49324) {
									// ㅃ
									consOne += "ㅃ";
									consTwo += "○";
								}else if(consInt >= 49324 && consInt < 49912) {
									// ㅅ
									consOne += "ㅅ";
									consTwo += "○";
								}else if(consInt >= 49912 && consInt < 50500) {
									// ㅆ
									consOne += "ㅆ";
									consTwo += "○";
								}else if(consInt >= 50500 && consInt < 51088) {
									// ㅇ
									consOne += "ㅇ";
									consTwo += "○";
								}else if(consInt >= 51088 && consInt < 51676) {
									// ㅈ
									consOne += "ㅈ";
									consTwo += "○";
								}else if(consInt >= 51676 && consInt < 52264) {
									// ㅉ
									consOne += "ㅉ";
									consTwo += "○";
								}else if(consInt >= 52264 && consInt < 52852) {
									// ㅊ
									consOne += "ㅊ";
									consTwo += "○";
								}else if(consInt >= 52852 && consInt < 53440) {
									// ㅋ
									consOne += "ㅋ";
									consTwo += "○";
								}else if(consInt >= 53440 && consInt < 54028) {
									// ㅌ
									consOne += "ㅌ";
									consTwo += "○";
								}else if(consInt >= 54028 && consInt < 54616) {
									// ㅍ
									consOne += "ㅍ";
									consTwo += "○";
								}else if(consInt >= 54616 && consInt < 55203) {
									// ㅎ
									consOne += "ㅎ";
									consTwo += "○";
								}else {
									// 한글이 아닌 문자 ('~', ' ')
									// 그대로 표현
									consOne += consChar;
									consTwo += consChar;
									
								}
								
								
							}
							
							consOne += lastTwo;
							consTwo += lastTwo;
							
							
							break;

						default:
							
							switch (lastTwo) {
							case "로는" : case "되는" : case "있는" : case "하는" :
							case "드는" : case "같은" : case "적인" : 
									
								for(int w = 0; w < vocaCons.get(k).length()-2; w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
									
								}
								
								consOne += lastTwo;
								consTwo += lastTwo;
								
								break;
							default:
								switch (lastOne) {
								case "는": case "은": case "의": case "한": case "인": 
								case "된": case "운": 
									
									for(int w = 0; w < vocaCons.get(k).length()-1; w++) {
										char consChar = vocaCons.get(k).charAt(w);
										int consInt = (int)consChar;
										
										if(consInt >= 44032 && consInt < 44620) {
											// ㄱ
											consOne += "ㄱ";
											consTwo += "○";
										}else if(consInt >= 44620 && consInt < 45208) {
											// ㄲ
											consOne += "ㄲ";
											consTwo += "○";
										}else if(consInt >= 45208 && consInt < 45796) {
											// ㄴ
											consOne += "ㄴ";
											consTwo += "○";
										}else if(consInt >= 45796 && consInt < 46384) {
											// ㄷ
											consOne += "ㄷ";
											consTwo += "○";
										}else if(consInt >= 46384 && consInt < 46972) {
											// ㄸ
											consOne += "ㄸ";
											consTwo += "○";
										}else if(consInt >= 46972 && consInt < 47560) {
											// ㄹ
											consOne += "ㄹ";
											consTwo += "○";
										}else if(consInt >= 47560 && consInt < 48148) {
											// ㅁ
											consOne += "ㅁ";
											consTwo += "○";
										}else if(consInt >= 48148 && consInt < 48736) {
											// ㅂ
											consOne += "ㅂ";
											consTwo += "○";
										}else if(consInt >= 48736 && consInt < 49324) {
											// ㅃ
											consOne += "ㅃ";
											consTwo += "○";
										}else if(consInt >= 49324 && consInt < 49912) {
											// ㅅ
											consOne += "ㅅ";
											consTwo += "○";
										}else if(consInt >= 49912 && consInt < 50500) {
											// ㅆ
											consOne += "ㅆ";
											consTwo += "○";
										}else if(consInt >= 50500 && consInt < 51088) {
											// ㅇ
											consOne += "ㅇ";
											consTwo += "○";
										}else if(consInt >= 51088 && consInt < 51676) {
											// ㅈ
											consOne += "ㅈ";
											consTwo += "○";
										}else if(consInt >= 51676 && consInt < 52264) {
											// ㅉ
											consOne += "ㅉ";
											consTwo += "○";
										}else if(consInt >= 52264 && consInt < 52852) {
											// ㅊ
											consOne += "ㅊ";
											consTwo += "○";
										}else if(consInt >= 52852 && consInt < 53440) {
											// ㅋ
											consOne += "ㅋ";
											consTwo += "○";
										}else if(consInt >= 53440 && consInt < 54028) {
											// ㅌ
											consOne += "ㅌ";
											consTwo += "○";
										}else if(consInt >= 54028 && consInt < 54616) {
											// ㅍ
											consOne += "ㅍ";
											consTwo += "○";
										}else if(consInt >= 54616 && consInt < 55203) {
											// ㅎ
											consOne += "ㅎ";
											consTwo += "○";
										}else {
											// 한글이 아닌 문자 ('~', ' ')
											// 그대로 표현
											consOne += consChar;
											consTwo += consChar;
											
										}
										
										
									}
									
									consOne += lastOne;
									consTwo += lastOne;
									
									break;
								default:
									
									for(int w = 0; w < vocaCons.get(k).length(); w++) {
										char consChar = vocaCons.get(k).charAt(w);
										int consInt = (int)consChar;
										
										if(consInt >= 44032 && consInt < 44620) {
											// ㄱ
											consOne += "ㄱ";
											consTwo += "○";
										}else if(consInt >= 44620 && consInt < 45208) {
											// ㄲ
											consOne += "ㄲ";
											consTwo += "○";
										}else if(consInt >= 45208 && consInt < 45796) {
											// ㄴ
											consOne += "ㄴ";
											consTwo += "○";
										}else if(consInt >= 45796 && consInt < 46384) {
											// ㄷ
											consOne += "ㄷ";
											consTwo += "○";
										}else if(consInt >= 46384 && consInt < 46972) {
											// ㄸ
											consOne += "ㄸ";
											consTwo += "○";
										}else if(consInt >= 46972 && consInt < 47560) {
											// ㄹ
											consOne += "ㄹ";
											consTwo += "○";
										}else if(consInt >= 47560 && consInt < 48148) {
											// ㅁ
											consOne += "ㅁ";
											consTwo += "○";
										}else if(consInt >= 48148 && consInt < 48736) {
											// ㅂ
											consOne += "ㅂ";
											consTwo += "○";
										}else if(consInt >= 48736 && consInt < 49324) {
											// ㅃ
											consOne += "ㅃ";
											consTwo += "○";
										}else if(consInt >= 49324 && consInt < 49912) {
											// ㅅ
											consOne += "ㅅ";
											consTwo += "○";
										}else if(consInt >= 49912 && consInt < 50500) {
											// ㅆ
											consOne += "ㅆ";
											consTwo += "○";
										}else if(consInt >= 50500 && consInt < 51088) {
											// ㅇ
											consOne += "ㅇ";
											consTwo += "○";
										}else if(consInt >= 51088 && consInt < 51676) {
											// ㅈ
											consOne += "ㅈ";
											consTwo += "○";
										}else if(consInt >= 51676 && consInt < 52264) {
											// ㅉ
											consOne += "ㅉ";
											consTwo += "○";
										}else if(consInt >= 52264 && consInt < 52852) {
											// ㅊ
											consOne += "ㅊ";
											consTwo += "○";
										}else if(consInt >= 52852 && consInt < 53440) {
											// ㅋ
											consOne += "ㅋ";
											consTwo += "○";
										}else if(consInt >= 53440 && consInt < 54028) {
											// ㅌ
											consOne += "ㅌ";
											consTwo += "○";
										}else if(consInt >= 54028 && consInt < 54616) {
											// ㅍ
											consOne += "ㅍ";
											consTwo += "○";
										}else if(consInt >= 54616 && consInt < 55203) {
											// ㅎ
											consOne += "ㅎ";
											consTwo += "○";
										}else {
											// 한글이 아닌 문자 ('~', ' ')
											// 그대로 표현
											consOne += consChar;
											consTwo += consChar;
											
										}
										
									}
									
									break;
								}
								
								break;
							}
				    		
				    		vocaConsOne.add(consOne);
				    		vocaConsTwo.add(consTwo);
							
							
							break;
						}
			    		
			    	}else if(voca.get(i).getVoPartOne().equals("adv")) {
			    		
			    		switch (lastTwo) {
						case "으로" : case "하게" : 
							
							for(int w = 0; w < vocaCons.get(k).length()-2; w++) {
								char consChar = vocaCons.get(k).charAt(w);
								int consInt = (int)consChar;
								
								if(consInt >= 44032 && consInt < 44620) {
									// ㄱ
									consOne += "ㄱ";
									consTwo += "○";
								}else if(consInt >= 44620 && consInt < 45208) {
									// ㄲ
									consOne += "ㄲ";
									consTwo += "○";
								}else if(consInt >= 45208 && consInt < 45796) {
									// ㄴ
									consOne += "ㄴ";
									consTwo += "○";
								}else if(consInt >= 45796 && consInt < 46384) {
									// ㄷ
									consOne += "ㄷ";
									consTwo += "○";
								}else if(consInt >= 46384 && consInt < 46972) {
									// ㄸ
									consOne += "ㄸ";
									consTwo += "○";
								}else if(consInt >= 46972 && consInt < 47560) {
									// ㄹ
									consOne += "ㄹ";
									consTwo += "○";
								}else if(consInt >= 47560 && consInt < 48148) {
									// ㅁ
									consOne += "ㅁ";
									consTwo += "○";
								}else if(consInt >= 48148 && consInt < 48736) {
									// ㅂ
									consOne += "ㅂ";
									consTwo += "○";
								}else if(consInt >= 48736 && consInt < 49324) {
									// ㅃ
									consOne += "ㅃ";
									consTwo += "○";
								}else if(consInt >= 49324 && consInt < 49912) {
									// ㅅ
									consOne += "ㅅ";
									consTwo += "○";
								}else if(consInt >= 49912 && consInt < 50500) {
									// ㅆ
									consOne += "ㅆ";
									consTwo += "○";
								}else if(consInt >= 50500 && consInt < 51088) {
									// ㅇ
									consOne += "ㅇ";
									consTwo += "○";
								}else if(consInt >= 51088 && consInt < 51676) {
									// ㅈ
									consOne += "ㅈ";
									consTwo += "○";
								}else if(consInt >= 51676 && consInt < 52264) {
									// ㅉ
									consOne += "ㅉ";
									consTwo += "○";
								}else if(consInt >= 52264 && consInt < 52852) {
									// ㅊ
									consOne += "ㅊ";
									consTwo += "○";
								}else if(consInt >= 52852 && consInt < 53440) {
									// ㅋ
									consOne += "ㅋ";
									consTwo += "○";
								}else if(consInt >= 53440 && consInt < 54028) {
									// ㅌ
									consOne += "ㅌ";
									consTwo += "○";
								}else if(consInt >= 54028 && consInt < 54616) {
									// ㅍ
									consOne += "ㅍ";
									consTwo += "○";
								}else if(consInt >= 54616 && consInt < 55203) {
									// ㅎ
									consOne += "ㅎ";
									consTwo += "○";
								}else {
									// 한글이 아닌 문자 ('~', ' ')
									// 그대로 표현
									consOne += consChar;
									consTwo += consChar;
									
								}
								
								
							}
							
							consOne += lastTwo;
							consTwo += lastTwo;
							
							break;
						default:
							switch (lastOne) {
							case "히": case "로": case "게": case "에": 
								
								for(int w = 0; w < vocaCons.get(k).length()-1; w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
									
								}
								
								consOne += lastOne;
								consTwo += lastOne;
								
								break;
							default:
								
								for(int w = 0; w < vocaCons.get(k).length(); w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
								}
								
								break;
							}
							
							break;
						}
			    		
			    		vocaConsOne.add(consOne);
			    		vocaConsTwo.add(consTwo);
			    		
			    		
			    		
			    	}else {
			    		
			    		for(int w = 0; w < vocaCons.get(k).length(); w++) {
							char consChar = vocaCons.get(k).charAt(w);
							int consInt = (int)consChar;
							
							if(consInt >= 44032 && consInt < 44620) {
								// ㄱ
								consOne += "ㄱ";
								consTwo += "○";
							}else if(consInt >= 44620 && consInt < 45208) {
								// ㄲ
								consOne += "ㄲ";
								consTwo += "○";
							}else if(consInt >= 45208 && consInt < 45796) {
								// ㄴ
								consOne += "ㄴ";
								consTwo += "○";
							}else if(consInt >= 45796 && consInt < 46384) {
								// ㄷ
								consOne += "ㄷ";
								consTwo += "○";
							}else if(consInt >= 46384 && consInt < 46972) {
								// ㄸ
								consOne += "ㄸ";
								consTwo += "○";
							}else if(consInt >= 46972 && consInt < 47560) {
								// ㄹ
								consOne += "ㄹ";
								consTwo += "○";
							}else if(consInt >= 47560 && consInt < 48148) {
								// ㅁ
								consOne += "ㅁ";
								consTwo += "○";
							}else if(consInt >= 48148 && consInt < 48736) {
								// ㅂ
								consOne += "ㅂ";
								consTwo += "○";
							}else if(consInt >= 48736 && consInt < 49324) {
								// ㅃ
								consOne += "ㅃ";
								consTwo += "○";
							}else if(consInt >= 49324 && consInt < 49912) {
								// ㅅ
								consOne += "ㅅ";
								consTwo += "○";
							}else if(consInt >= 49912 && consInt < 50500) {
								// ㅆ
								consOne += "ㅆ";
								consTwo += "○";
							}else if(consInt >= 50500 && consInt < 51088) {
								// ㅇ
								consOne += "ㅇ";
								consTwo += "○";
							}else if(consInt >= 51088 && consInt < 51676) {
								// ㅈ
								consOne += "ㅈ";
								consTwo += "○";
							}else if(consInt >= 51676 && consInt < 52264) {
								// ㅉ
								consOne += "ㅉ";
								consTwo += "○";
							}else if(consInt >= 52264 && consInt < 52852) {
								// ㅊ
								consOne += "ㅊ";
								consTwo += "○";
							}else if(consInt >= 52852 && consInt < 53440) {
								// ㅋ
								consOne += "ㅋ";
								consTwo += "○";
							}else if(consInt >= 53440 && consInt < 54028) {
								// ㅌ
								consOne += "ㅌ";
								consTwo += "○";
							}else if(consInt >= 54028 && consInt < 54616) {
								// ㅍ
								consOne += "ㅍ";
								consTwo += "○";
							}else if(consInt >= 54616 && consInt < 55203) {
								// ㅎ
								consOne += "ㅎ";
								consTwo += "○";
							}else {
								// 한글이 아닌 문자 ('~', ' ')
								// 그대로 표현
								consOne += consChar;
								consTwo += consChar;
								
							}
							
						}
			    		
			    		vocaConsOne.add(consOne);
			    		vocaConsTwo.add(consTwo);
			    	}
		    		
		    	}else {
		    		// 두개 다 고려
		    		


		    		// 동사일 때
		    		if(voca.get(i).getVoPartOne().equals("v") || voca.get(i).getVoPartTwo().equals("v")) {
		    			
		    			
			    		switch (lastTwo) {
						case "하다" : case "주다" : case "이다" : case "보다" :
						case "되다" :
							
							for(int w = 0; w < vocaCons.get(k).length()-2; w++) {
								char consChar = vocaCons.get(k).charAt(w);
								int consInt = (int)consChar;
								
								if(consInt >= 44032 && consInt < 44620) {
									// ㄱ
									consOne += "ㄱ";
									consTwo += "○";
								}else if(consInt >= 44620 && consInt < 45208) {
									// ㄲ
									consOne += "ㄲ";
									consTwo += "○";
								}else if(consInt >= 45208 && consInt < 45796) {
									// ㄴ
									consOne += "ㄴ";
									consTwo += "○";
								}else if(consInt >= 45796 && consInt < 46384) {
									// ㄷ
									consOne += "ㄷ";
									consTwo += "○";
								}else if(consInt >= 46384 && consInt < 46972) {
									// ㄸ
									consOne += "ㄸ";
									consTwo += "○";
								}else if(consInt >= 46972 && consInt < 47560) {
									// ㄹ
									consOne += "ㄹ";
									consTwo += "○";
								}else if(consInt >= 47560 && consInt < 48148) {
									// ㅁ
									consOne += "ㅁ";
									consTwo += "○";
								}else if(consInt >= 48148 && consInt < 48736) {
									// ㅂ
									consOne += "ㅂ";
									consTwo += "○";
								}else if(consInt >= 48736 && consInt < 49324) {
									// ㅃ
									consOne += "ㅃ";
									consTwo += "○";
								}else if(consInt >= 49324 && consInt < 49912) {
									// ㅅ
									consOne += "ㅅ";
									consTwo += "○";
								}else if(consInt >= 49912 && consInt < 50500) {
									// ㅆ
									consOne += "ㅆ";
									consTwo += "○";
								}else if(consInt >= 50500 && consInt < 51088) {
									// ㅇ
									consOne += "ㅇ";
									consTwo += "○";
								}else if(consInt >= 51088 && consInt < 51676) {
									// ㅈ
									consOne += "ㅈ";
									consTwo += "○";
								}else if(consInt >= 51676 && consInt < 52264) {
									// ㅉ
									consOne += "ㅉ";
									consTwo += "○";
								}else if(consInt >= 52264 && consInt < 52852) {
									// ㅊ
									consOne += "ㅊ";
									consTwo += "○";
								}else if(consInt >= 52852 && consInt < 53440) {
									// ㅋ
									consOne += "ㅋ";
									consTwo += "○";
								}else if(consInt >= 53440 && consInt < 54028) {
									// ㅌ
									consOne += "ㅌ";
									consTwo += "○";
								}else if(consInt >= 54028 && consInt < 54616) {
									// ㅍ
									consOne += "ㅍ";
									consTwo += "○";
								}else if(consInt >= 54616 && consInt < 55203) {
									// ㅎ
									consOne += "ㅎ";
									consTwo += "○";
								}else {
									// 한글이 아닌 문자 ('~', ' ')
									// 그대로 표현
									consOne += consChar;
									consTwo += consChar;
									
								}
								
								
							}
							
							consOne += lastTwo;
							consTwo += lastTwo;
							
							break;
						default:
							switch (lastOne) {
							case "다": 
								
								for(int w = 0; w < vocaCons.get(k).length()-1; w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
									
								}
								
								consOne += lastOne;
								consTwo += lastOne;
								
								break;
							default:
								
								
								break;
							}
							
							break;
						}
			    		
			    		
			    	}
		    		
		    		if(consOne == "" && (voca.get(i).getVoPartOne().equals("adj") || voca.get(i).getVoPartTwo().equals("adj"))) {
			    		
			    		switch (lastThree) {
						case "으로는":
							
							for(int w = 0; w < vocaCons.get(k).length()-3; w++) {
								char consChar = vocaCons.get(k).charAt(w);
								int consInt = (int)consChar;
								
								if(consInt >= 44032 && consInt < 44620) {
									// ㄱ
									consOne += "ㄱ";
									consTwo += "○";
								}else if(consInt >= 44620 && consInt < 45208) {
									// ㄲ
									consOne += "ㄲ";
									consTwo += "○";
								}else if(consInt >= 45208 && consInt < 45796) {
									// ㄴ
									consOne += "ㄴ";
									consTwo += "○";
								}else if(consInt >= 45796 && consInt < 46384) {
									// ㄷ
									consOne += "ㄷ";
									consTwo += "○";
								}else if(consInt >= 46384 && consInt < 46972) {
									// ㄸ
									consOne += "ㄸ";
									consTwo += "○";
								}else if(consInt >= 46972 && consInt < 47560) {
									// ㄹ
									consOne += "ㄹ";
									consTwo += "○";
								}else if(consInt >= 47560 && consInt < 48148) {
									// ㅁ
									consOne += "ㅁ";
									consTwo += "○";
								}else if(consInt >= 48148 && consInt < 48736) {
									// ㅂ
									consOne += "ㅂ";
									consTwo += "○";
								}else if(consInt >= 48736 && consInt < 49324) {
									// ㅃ
									consOne += "ㅃ";
									consTwo += "○";
								}else if(consInt >= 49324 && consInt < 49912) {
									// ㅅ
									consOne += "ㅅ";
									consTwo += "○";
								}else if(consInt >= 49912 && consInt < 50500) {
									// ㅆ
									consOne += "ㅆ";
									consTwo += "○";
								}else if(consInt >= 50500 && consInt < 51088) {
									// ㅇ
									consOne += "ㅇ";
									consTwo += "○";
								}else if(consInt >= 51088 && consInt < 51676) {
									// ㅈ
									consOne += "ㅈ";
									consTwo += "○";
								}else if(consInt >= 51676 && consInt < 52264) {
									// ㅉ
									consOne += "ㅉ";
									consTwo += "○";
								}else if(consInt >= 52264 && consInt < 52852) {
									// ㅊ
									consOne += "ㅊ";
									consTwo += "○";
								}else if(consInt >= 52852 && consInt < 53440) {
									// ㅋ
									consOne += "ㅋ";
									consTwo += "○";
								}else if(consInt >= 53440 && consInt < 54028) {
									// ㅌ
									consOne += "ㅌ";
									consTwo += "○";
								}else if(consInt >= 54028 && consInt < 54616) {
									// ㅍ
									consOne += "ㅍ";
									consTwo += "○";
								}else if(consInt >= 54616 && consInt < 55203) {
									// ㅎ
									consOne += "ㅎ";
									consTwo += "○";
								}else {
									// 한글이 아닌 문자 ('~', ' ')
									// 그대로 표현
									consOne += consChar;
									consTwo += consChar;
									
								}
								
								
							}
							
							consOne += lastTwo;
							consTwo += lastTwo;
							
							
							break;

						default:
							
							switch (lastTwo) {
							case "로는" : case "되는" : case "있는" : case "하는" :
							case "드는" : case "같은" : case "적인" : 
									
								for(int w = 0; w < vocaCons.get(k).length()-2; w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
									
								}
								
								consOne += lastTwo;
								consTwo += lastTwo;
								
								break;
							default:
								switch (lastOne) {
								case "는": case "은": case "의": case "한": case "인": 
								case "된": case "운": 
									
									for(int w = 0; w < vocaCons.get(k).length()-1; w++) {
										char consChar = vocaCons.get(k).charAt(w);
										int consInt = (int)consChar;
										
										if(consInt >= 44032 && consInt < 44620) {
											// ㄱ
											consOne += "ㄱ";
											consTwo += "○";
										}else if(consInt >= 44620 && consInt < 45208) {
											// ㄲ
											consOne += "ㄲ";
											consTwo += "○";
										}else if(consInt >= 45208 && consInt < 45796) {
											// ㄴ
											consOne += "ㄴ";
											consTwo += "○";
										}else if(consInt >= 45796 && consInt < 46384) {
											// ㄷ
											consOne += "ㄷ";
											consTwo += "○";
										}else if(consInt >= 46384 && consInt < 46972) {
											// ㄸ
											consOne += "ㄸ";
											consTwo += "○";
										}else if(consInt >= 46972 && consInt < 47560) {
											// ㄹ
											consOne += "ㄹ";
											consTwo += "○";
										}else if(consInt >= 47560 && consInt < 48148) {
											// ㅁ
											consOne += "ㅁ";
											consTwo += "○";
										}else if(consInt >= 48148 && consInt < 48736) {
											// ㅂ
											consOne += "ㅂ";
											consTwo += "○";
										}else if(consInt >= 48736 && consInt < 49324) {
											// ㅃ
											consOne += "ㅃ";
											consTwo += "○";
										}else if(consInt >= 49324 && consInt < 49912) {
											// ㅅ
											consOne += "ㅅ";
											consTwo += "○";
										}else if(consInt >= 49912 && consInt < 50500) {
											// ㅆ
											consOne += "ㅆ";
											consTwo += "○";
										}else if(consInt >= 50500 && consInt < 51088) {
											// ㅇ
											consOne += "ㅇ";
											consTwo += "○";
										}else if(consInt >= 51088 && consInt < 51676) {
											// ㅈ
											consOne += "ㅈ";
											consTwo += "○";
										}else if(consInt >= 51676 && consInt < 52264) {
											// ㅉ
											consOne += "ㅉ";
											consTwo += "○";
										}else if(consInt >= 52264 && consInt < 52852) {
											// ㅊ
											consOne += "ㅊ";
											consTwo += "○";
										}else if(consInt >= 52852 && consInt < 53440) {
											// ㅋ
											consOne += "ㅋ";
											consTwo += "○";
										}else if(consInt >= 53440 && consInt < 54028) {
											// ㅌ
											consOne += "ㅌ";
											consTwo += "○";
										}else if(consInt >= 54028 && consInt < 54616) {
											// ㅍ
											consOne += "ㅍ";
											consTwo += "○";
										}else if(consInt >= 54616 && consInt < 55203) {
											// ㅎ
											consOne += "ㅎ";
											consTwo += "○";
										}else {
											// 한글이 아닌 문자 ('~', ' ')
											// 그대로 표현
											consOne += consChar;
											consTwo += consChar;
											
										}
										
										
									}
									
									consOne += lastOne;
									consTwo += lastOne;
									
									break;
								default:
									
									
									break;
								}
								
								break;
							}
				    		
							
							
							break;
						}
			    		
			    	}
		    		
		    		if(consOne == "" && (voca.get(i).getVoPartOne().equals("adv") || voca.get(i).getVoPartTwo().equals("adv"))) {
			    		
			    		switch (lastTwo) {
						case "으로" : case "하게" : 
							
							for(int w = 0; w < vocaCons.get(k).length()-2; w++) {
								char consChar = vocaCons.get(k).charAt(w);
								int consInt = (int)consChar;
								
								if(consInt >= 44032 && consInt < 44620) {
									// ㄱ
									consOne += "ㄱ";
									consTwo += "○";
								}else if(consInt >= 44620 && consInt < 45208) {
									// ㄲ
									consOne += "ㄲ";
									consTwo += "○";
								}else if(consInt >= 45208 && consInt < 45796) {
									// ㄴ
									consOne += "ㄴ";
									consTwo += "○";
								}else if(consInt >= 45796 && consInt < 46384) {
									// ㄷ
									consOne += "ㄷ";
									consTwo += "○";
								}else if(consInt >= 46384 && consInt < 46972) {
									// ㄸ
									consOne += "ㄸ";
									consTwo += "○";
								}else if(consInt >= 46972 && consInt < 47560) {
									// ㄹ
									consOne += "ㄹ";
									consTwo += "○";
								}else if(consInt >= 47560 && consInt < 48148) {
									// ㅁ
									consOne += "ㅁ";
									consTwo += "○";
								}else if(consInt >= 48148 && consInt < 48736) {
									// ㅂ
									consOne += "ㅂ";
									consTwo += "○";
								}else if(consInt >= 48736 && consInt < 49324) {
									// ㅃ
									consOne += "ㅃ";
									consTwo += "○";
								}else if(consInt >= 49324 && consInt < 49912) {
									// ㅅ
									consOne += "ㅅ";
									consTwo += "○";
								}else if(consInt >= 49912 && consInt < 50500) {
									// ㅆ
									consOne += "ㅆ";
									consTwo += "○";
								}else if(consInt >= 50500 && consInt < 51088) {
									// ㅇ
									consOne += "ㅇ";
									consTwo += "○";
								}else if(consInt >= 51088 && consInt < 51676) {
									// ㅈ
									consOne += "ㅈ";
									consTwo += "○";
								}else if(consInt >= 51676 && consInt < 52264) {
									// ㅉ
									consOne += "ㅉ";
									consTwo += "○";
								}else if(consInt >= 52264 && consInt < 52852) {
									// ㅊ
									consOne += "ㅊ";
									consTwo += "○";
								}else if(consInt >= 52852 && consInt < 53440) {
									// ㅋ
									consOne += "ㅋ";
									consTwo += "○";
								}else if(consInt >= 53440 && consInt < 54028) {
									// ㅌ
									consOne += "ㅌ";
									consTwo += "○";
								}else if(consInt >= 54028 && consInt < 54616) {
									// ㅍ
									consOne += "ㅍ";
									consTwo += "○";
								}else if(consInt >= 54616 && consInt < 55203) {
									// ㅎ
									consOne += "ㅎ";
									consTwo += "○";
								}else {
									// 한글이 아닌 문자 ('~', ' ')
									// 그대로 표현
									consOne += consChar;
									consTwo += consChar;
									
								}
								
								
							}
							
							consOne += lastTwo;
							consTwo += lastTwo;
							
							break;
						default:
							switch (lastOne) {
							case "히": case "로": case "게": case "에": 
								
								for(int w = 0; w < vocaCons.get(k).length()-1; w++) {
									char consChar = vocaCons.get(k).charAt(w);
									int consInt = (int)consChar;
									
									if(consInt >= 44032 && consInt < 44620) {
										// ㄱ
										consOne += "ㄱ";
										consTwo += "○";
									}else if(consInt >= 44620 && consInt < 45208) {
										// ㄲ
										consOne += "ㄲ";
										consTwo += "○";
									}else if(consInt >= 45208 && consInt < 45796) {
										// ㄴ
										consOne += "ㄴ";
										consTwo += "○";
									}else if(consInt >= 45796 && consInt < 46384) {
										// ㄷ
										consOne += "ㄷ";
										consTwo += "○";
									}else if(consInt >= 46384 && consInt < 46972) {
										// ㄸ
										consOne += "ㄸ";
										consTwo += "○";
									}else if(consInt >= 46972 && consInt < 47560) {
										// ㄹ
										consOne += "ㄹ";
										consTwo += "○";
									}else if(consInt >= 47560 && consInt < 48148) {
										// ㅁ
										consOne += "ㅁ";
										consTwo += "○";
									}else if(consInt >= 48148 && consInt < 48736) {
										// ㅂ
										consOne += "ㅂ";
										consTwo += "○";
									}else if(consInt >= 48736 && consInt < 49324) {
										// ㅃ
										consOne += "ㅃ";
										consTwo += "○";
									}else if(consInt >= 49324 && consInt < 49912) {
										// ㅅ
										consOne += "ㅅ";
										consTwo += "○";
									}else if(consInt >= 49912 && consInt < 50500) {
										// ㅆ
										consOne += "ㅆ";
										consTwo += "○";
									}else if(consInt >= 50500 && consInt < 51088) {
										// ㅇ
										consOne += "ㅇ";
										consTwo += "○";
									}else if(consInt >= 51088 && consInt < 51676) {
										// ㅈ
										consOne += "ㅈ";
										consTwo += "○";
									}else if(consInt >= 51676 && consInt < 52264) {
										// ㅉ
										consOne += "ㅉ";
										consTwo += "○";
									}else if(consInt >= 52264 && consInt < 52852) {
										// ㅊ
										consOne += "ㅊ";
										consTwo += "○";
									}else if(consInt >= 52852 && consInt < 53440) {
										// ㅋ
										consOne += "ㅋ";
										consTwo += "○";
									}else if(consInt >= 53440 && consInt < 54028) {
										// ㅌ
										consOne += "ㅌ";
										consTwo += "○";
									}else if(consInt >= 54028 && consInt < 54616) {
										// ㅍ
										consOne += "ㅍ";
										consTwo += "○";
									}else if(consInt >= 54616 && consInt < 55203) {
										// ㅎ
										consOne += "ㅎ";
										consTwo += "○";
									}else {
										// 한글이 아닌 문자 ('~', ' ')
										// 그대로 표현
										consOne += consChar;
										consTwo += consChar;
										
									}
									
									
								}
								
								consOne += lastOne;
								consTwo += lastOne;
								
								break;
							default:
								
								
								break;
							}
							
							break;
						}
			    		
			    		
			    		
			    		
			    		
			    	}
		    		
		    		// 동사, 형용사, 부사에 해당 안될 때 (-> 명사 등..)
		    		if(consOne == ""){
		    			
			    		for(int w = 0; w < vocaCons.get(k).length(); w++) {
							char consChar = vocaCons.get(k).charAt(w);
							int consInt = (int)consChar;
							
							if(consInt >= 44032 && consInt < 44620) {
								// ㄱ
								consOne += "ㄱ";
								consTwo += "○";
							}else if(consInt >= 44620 && consInt < 45208) {
								// ㄲ
								consOne += "ㄲ";
								consTwo += "○";
							}else if(consInt >= 45208 && consInt < 45796) {
								// ㄴ
								consOne += "ㄴ";
								consTwo += "○";
							}else if(consInt >= 45796 && consInt < 46384) {
								// ㄷ
								consOne += "ㄷ";
								consTwo += "○";
							}else if(consInt >= 46384 && consInt < 46972) {
								// ㄸ
								consOne += "ㄸ";
								consTwo += "○";
							}else if(consInt >= 46972 && consInt < 47560) {
								// ㄹ
								consOne += "ㄹ";
								consTwo += "○";
							}else if(consInt >= 47560 && consInt < 48148) {
								// ㅁ
								consOne += "ㅁ";
								consTwo += "○";
							}else if(consInt >= 48148 && consInt < 48736) {
								// ㅂ
								consOne += "ㅂ";
								consTwo += "○";
							}else if(consInt >= 48736 && consInt < 49324) {
								// ㅃ
								consOne += "ㅃ";
								consTwo += "○";
							}else if(consInt >= 49324 && consInt < 49912) {
								// ㅅ
								consOne += "ㅅ";
								consTwo += "○";
							}else if(consInt >= 49912 && consInt < 50500) {
								// ㅆ
								consOne += "ㅆ";
								consTwo += "○";
							}else if(consInt >= 50500 && consInt < 51088) {
								// ㅇ
								consOne += "ㅇ";
								consTwo += "○";
							}else if(consInt >= 51088 && consInt < 51676) {
								// ㅈ
								consOne += "ㅈ";
								consTwo += "○";
							}else if(consInt >= 51676 && consInt < 52264) {
								// ㅉ
								consOne += "ㅉ";
								consTwo += "○";
							}else if(consInt >= 52264 && consInt < 52852) {
								// ㅊ
								consOne += "ㅊ";
								consTwo += "○";
							}else if(consInt >= 52852 && consInt < 53440) {
								// ㅋ
								consOne += "ㅋ";
								consTwo += "○";
							}else if(consInt >= 53440 && consInt < 54028) {
								// ㅌ
								consOne += "ㅌ";
								consTwo += "○";
							}else if(consInt >= 54028 && consInt < 54616) {
								// ㅍ
								consOne += "ㅍ";
								consTwo += "○";
							}else if(consInt >= 54616 && consInt < 55203) {
								// ㅎ
								consOne += "ㅎ";
								consTwo += "○";
							}else {
								// 한글이 아닌 문자 ('~', ' ')
								// 그대로 표현
								consOne += consChar;
								consTwo += consChar;
								
							}
							
						}
			    		
			    		
			    	}
		    		
		    		vocaConsOne.add(consOne);
		    		vocaConsTwo.add(consTwo);
		    		
		    	}
	    		
	    		
	    		
	    	}
	    	// vacaCons에 정리 완료
	    	
	    	
	    	
	    	
	    	
	    	// 3개 초과될 경우 3개만 뽑아서 정리
	    	if(vocaCons.size() > 3) {
	    		List<String> threeCons = new ArrayList<>();
		    	List<String> threeConsOne = new ArrayList<>();
		    	List<String> threeConsTwo = new ArrayList<>();
		    	
		    	String ranStr2 = "";
		        int ranCount2 = 0;
		        for( int j = (int) Math.floor(Math.random() * vocaCons.size()) ; ranCount2 < 3 ; ){

		        	threeCons.add(vocaCons.get(j));
		        	threeConsOne.add(vocaConsOne.get(j));
		        	threeConsTwo.add(vocaConsTwo.get(j));
		        	
		            // random 작업 ------
		            // i = 0 ~ size-1 까지
		            ranStr2 += " "+ j + " ";
		            ranCount2++;
		            while( ranStr2.indexOf(" "+ j + " ",0) != -1 && ranCount2 < 3){
		                j = (int) Math.floor(Math.random() * vocaCons.size());
		            }
		        }
		        
		        vocaMmz.setHwCons(threeCons);
		    	vocaMmz.setHwConsOne(threeConsOne);
		    	vocaMmz.setHwConsTwo(threeConsTwo);
		    	
	    	}else {
	    		vocaMmz.setHwCons(vocaCons);
		    	vocaMmz.setHwConsOne(vocaConsOne);
		    	vocaMmz.setHwConsTwo(vocaConsTwo);
	    	}
	    	
	    	
	    	vocaMmzList.add(vocaMmz);
			
		}

		mav.addObject("vocaMmzList", vocaMmzList);

		mav.setViewName("vocaTraining");

		return mav;
	}
    

    @Override
    public ModelAndView quizResult(int dirNum, String input) {

        mav = new ModelAndView();

        VocaDTO voca = new VocaDTO();

        List<VocaCardDTO> vocaCardList = new ArrayList<>();
        List<VocaCardDTO> vocaQuizList = new ArrayList<>();

        int vocaBool = input.lastIndexOf("_");
        while (vocaBool != -1){
            int inputIndex = 0;
            int sumIndex = 0;
            int voNumIndex = 0;

            if(vocaBool != -1){
                inputIndex = input.lastIndexOf('_',vocaBool-1);
                sumIndex = input.lastIndexOf('@',vocaBool);
                voNumIndex = input.lastIndexOf('*',vocaBool);

                voca.setVoNum(Integer.parseInt(input.substring(inputIndex+1,voNumIndex)));
                int sum = Integer.parseInt(input.substring(voNumIndex+1,sumIndex));
                String inputHw = input.substring(sumIndex+1,vocaBool);

                voca = vdao.vocaViewOne(voca.getVoNum());

                VocaCardDTO vocaCard = new VocaCardDTO();
                vocaCard.setVoDirNum(dirNum);
                vocaCard.setVoNum(voca.getVoNum());
                vocaCard.setVoTbName(voca.getVoTbName());
                vocaCard.setVoSp(voca.getVoSp());
                vocaCard.setVoHw(voca.getVoHw());
                vocaCard.setVoImg(voca.getVoImg());
                vocaCard.setVoInput(inputHw);
                vocaCard.setSum(sum);

                vocaCardList.add(vocaCard);


            }
            vocaBool = input.lastIndexOf("_", vocaBool-1);

        }

        for(int i = vocaCardList.size()-1 ; i >= 0 ; i--){
            vocaQuizList.add(vocaCardList.get(i));
        }

        mav.setViewName("quizResult2");
        mav.addObject("vocaCardList", vocaQuizList);

        return mav;
    }

    @Override
    public String dirNumCheck() {

        return vdao.dirNumCheck();
    }

    @Override
    public List<VocaDTO> searching(int dirNum) {

        List<VocaDTO> vocaList = new ArrayList<>();

        int count =  vdao.searchCount(dirNum);

        System.out.println("dirNum : " + dirNum);
        System.out.println("count : " + count);
        if(count == 0){
            return null;
        }

        VocaRowDTO vocaRN = new VocaRowDTO();
        int startNum = (count-5) < 1 ? 1 : count-5;
        int endNum = count;
        vocaRN.setVoDirNum(dirNum);
        vocaRN.setStartNum(startNum);
        vocaRN.setEndNum(endNum);

        System.out.println("startNum"+ startNum);
        System.out.println("endNum"+ endNum);

        vocaList = vdao.vocaSearch(vocaRN);


        for(int i = 0; i < endNum-startNum+1 ; i++){
            vocaList.get(i).setVoImgLv(startNum+i);
        }

        System.out.println(vocaList);


        return vocaList;
    }

    @Override
    public String randomVoca() {

        VocaDTO voca = new VocaDTO();

        int allCount = vdao.allCount();

        int ranNum = (int) Math.floor(Math.random()* allCount + 1);

        voca = vdao.vocaRanOne(ranNum);

        System.out.println(voca.getVoSp());

        return voca.getVoSp();
    }

    @Override
    public ModelAndView vocaTenQuiz(int dirNum, int type) {

        mav = vocaCard(dirNum, type);
        mav.setViewName("tenQuiz");

        return mav;
    }




}
