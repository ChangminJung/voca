package com.personal.voca.service;

import com.personal.voca.dao.VocaDAO;
import com.personal.voca.dto.*;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VocaServiceImpl implements VocaService {

    private ModelAndView mav;

    private final HttpSession session;

    private final VocaDAO vdao;

    @Override
    public ModelAndView vocaSubmit(String tbName, String input) {

        mav = new ModelAndView();
        input = input.toLowerCase();
        VocaDTO voca = new VocaDTO();
        voca.setVoTbName(tbName);
        LocalDateTime date = LocalDateTime.now();
        voca.setVoDate(date.toString().substring(0,10));
        vdao.voDirSubmit(voca);

        String imgHtml = "";
        String imgCode = "";
        String imgSrc = "";
        int codeIndex = 0;
        int codeIndex2 = 0;
        int codeIndex3 = 0;
        int vocaBool = input.lastIndexOf("_");
        while (vocaBool != -1){
            int spIndex = 0;
            int hwIndex = 0;

            if(vocaBool != -1){
                spIndex = input.lastIndexOf('_',vocaBool-1);
                hwIndex = input.lastIndexOf('/',vocaBool);

                voca.setVoSp(input.substring(spIndex+1,hwIndex));
                voca.setVoHw(input.substring(hwIndex+1,vocaBool));

                try{    // 사진 크롤링
                    String gooCrl = "https://www.google.com/search?q=" + voca.getVoSp() +
                            "&sxsrf=AJOqlzXtMxXOFqrgfJCoFzOIwd9coYYceg:1679550712694&source=lnms&tbm=isch&sa=X&ved=2ahUKEwifxfL8rfH9AhU9cGwGHUoSC50Q_AUoAXoECAEQAw&biw=1051&bih=948&dpr=1.5";
                    Document doc = Jsoup.connect(gooCrl).get();
                    Elements eUrl = doc.select("div#is-results div");
                    imgCode = eUrl.get(0).attr("data-id");

                    gooCrl = "https://www.google.com/search?q=" + voca.getVoSp()
                            + "&sxsrf=AJOqlzWu_FCVKeRgGiqZJnpNK8fcGcKscQ:1679595930292&source=lnms&tbm=isch&sa=X&ved=2ahUKEwj96am21vL9AhW_XWwGHcF5DLcQ_AUoAXoECAEQAw&biw=1707&bih=948&dpr=1.5#imgrc=" + imgCode;

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

                vdao.vocaSubmit(voca);
            }
            vocaBool = input.lastIndexOf("_", vocaBool-1);

        }
        mav.setViewName("home");

        return mav;
    }

    @Override
    public ModelAndView vocaTable() {

        mav = new ModelAndView();

        List<VocaTableDTO> vocaTable = new ArrayList<>();

        vocaTable = vdao.vocaTable();
        System.out.println(vocaTable);
        mav.setViewName("vocaTable");
        mav.addObject("vocaTable", vocaTable);

        return mav;
    }

    @Override
    public ModelAndView vocaView(int dirNum) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaDTO> vocaCheck = new ArrayList<>();

        voca = vdao.vocaView(dirNum);
        for(int i = 0; i < voca.size(); i++) {
            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(",", ", "));
            voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(";", "; "));

        }
        vocaCheck = vdao.vocaCheckView(dirNum);
        for(int i = 0; i < vocaCheck.size(); i++) {
            vocaCheck.get(i).setVoHw(vocaCheck.get(i).getVoHw().replaceAll(",", ", "));
            vocaCheck.get(i).setVoHw(vocaCheck.get(i).getVoHw().replaceAll(";", "; "));
        }


        mav.setViewName("vocaView");
        mav.addObject("voca", voca);
        mav.addObject("vocaCheck", vocaCheck);


        // 단어장 만들기용
        String input = "vocaSubmit?tbName=Day10&input=";
        for(int i = 0; i < voca.size(); i++){
            input+= voca.get(i).getVoSp() + "/" + voca.get(i).getVoHw() + "_";
        }
//        System.out.println(input);


        return mav;
    }

    @Override
    public List<List<VocaDTO>> checkSave(String input, int dirNum) {

        List<List<VocaDTO>> vocaData = new ArrayList<>();
        List<VocaDTO> vocaList = new ArrayList<>();
        List<VocaDTO> vocaCheck = new ArrayList<>();

        int vocaBool = input.lastIndexOf("_");
        while (vocaBool != -1){
            VocaDTO voca = new VocaDTO();

            int checkIndex = 0;
            int voNumIndex = 0;

            if(vocaBool != -1){
                checkIndex = input.lastIndexOf('_',vocaBool-1);
                voNumIndex = input.lastIndexOf('/',vocaBool);

                voca.setVoNum(Integer.parseInt(input.substring(checkIndex+1,voNumIndex)));
                voca.setVoCheck(Integer.parseInt(input.substring(voNumIndex+1,vocaBool)));

                vdao.checkSave(voca);

            }
            vocaBool = input.lastIndexOf("_", vocaBool-1);
        }

        vocaList = vdao.vocaView(dirNum);
        vocaCheck = vdao.vocaCheckView(dirNum);

        vocaData.add(vocaList);
        vocaData.add(vocaCheck);

        return vocaData;
    }

    @Override
    public ModelAndView vocaTest(int dirNum, int level, int type) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaTestDTO> vocaTestList = new ArrayList<>();

        if(type == 0){
            voca = vdao.vocaView(dirNum);
        }else{
            voca = vdao.vocaCheckView(dirNum);
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
            voca = vdao.vocaCheckView(dirNum);
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

        mav.setViewName("vocaCard");
        mav.addObject("vocaCardList", vocaCardList);
        mav.addObject("type", type);


        return mav;
    }

    @Override
    public String imgSearch(String input) {

        List<String> imgList = new ArrayList<>();

        if(input != null && input != ""){

            input = input.substring(0,input.length()-1);
            imgList = List.of(input.split(" "));

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
                    String gooCrl = "https://www.google.com/search?q=" + voca.getVoSp() +
                            "&sxsrf=AJOqlzXtMxXOFqrgfJCoFzOIwd9coYYceg:1679550712694&source=lnms&tbm=isch&sa=X&ved=2ahUKEwifxfL8rfH9AhU9cGwGHUoSC50Q_AUoAXoECAEQAw&biw=1051&bih=948&dpr=1.5";
                    Document doc = Jsoup.connect(gooCrl).get();
                    Elements eUrl = doc.select("div#is-results div");
                    imgCode = eUrl.get(imgLevel).attr("data-id");

                    while (imgCode == null || imgCode == "") {
                        imgLevel++;
                        imgCode = eUrl.get(imgLevel).attr("data-id");
                    }

                    gooCrl = "https://www.google.com/search?q=" + voca.getVoSp()
                            + "&sxsrf=AJOqlzWu_FCVKeRgGiqZJnpNK8fcGcKscQ:1679595930292&source=lnms&tbm=isch&sa=X&ved=2ahUKEwj96am21vL9AhW_XWwGHcF5DLcQ_AUoAXoECAEQAw&biw=1707&bih=948&dpr=1.5#imgrc=" + imgCode;

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
    public ModelAndView vocaModify(String tbName, int dirNum, String input) {

        mav = new ModelAndView();

        VocaDTO voca = new VocaDTO();
        voca.setVoDirNum(dirNum);
        voca.setVoTbName(tbName);
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
                    "&sxsrf=AJOqlzXtMxXOFqrgfJCoFzOIwd9coYYceg:1679550712694&source=lnms&tbm=isch&sa=X&ved=2ahUKEwifxfL8rfH9AhU9cGwGHUoSC50Q_AUoAXoECAEQAw&biw=1051&bih=948&dpr=1.5";
            Document doc = Jsoup.connect(gooCrl).get();
            Elements eUrl = doc.select("div#is-results div");
            for(int i = imgLevel; i < imgLevel+5 ; i++){
                imgCode[i-imgLevel] = eUrl.get(i).attr("data-id");

                while (imgCode[i-imgLevel] == null || imgCode[i-imgLevel] == "") {
                    imgLevel++;
                    i++;
                    imgCode[i-imgLevel] = eUrl.get(i).attr("data-id");
                }
                imgLv.add(imgLevel);
            }


            for(int i = 0; i < 5; i ++){
                gooCrl = "https://www.google.com/search?q=" + voca.getVoSp()
                        + "&sxsrf=AJOqlzWu_FCVKeRgGiqZJnpNK8fcGcKscQ:1679595930292&source=lnms&tbm=isch&sa=X&ved=2ahUKEwj96am21vL9AhW_XWwGHcF5DLcQ_AUoAXoECAEQAw&biw=1707&bih=948&dpr=1.5#imgrc=" + imgCode[i];

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
            voca = vdao.vocaCheckView(dirNum);
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
