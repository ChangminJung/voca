package com.personal.voca.service;

import com.personal.voca.dao.VocaDAO;
import com.personal.voca.dto.VocaCardDTO;
import com.personal.voca.dto.VocaDTO;
import com.personal.voca.dto.VocaTableDTO;
import com.personal.voca.dto.VocaTestDTO;
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
        input = input.replaceAll(" ","");
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
                    imgHtml = eUrl.get(eUrl.size()-5).html();

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
        voca = vdao.vocaView(dirNum);
        for(int i = 0; i < voca.size(); i++){
           voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(",",", "));
           voca.get(i).setVoHw(voca.get(i).getVoHw().replaceAll(";","; "));
        }
        System.out.println(voca);
        mav.setViewName("vocaView");
        mav.addObject("voca", voca);






        return mav;
    }

    @Override
    public ModelAndView vocaTest(int dirNum, int level) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaTestDTO> vocaTestList = new ArrayList<>();

        voca = vdao.vocaView(dirNum);
        System.out.println(voca.size());
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

        return mav;
    }

    @Override
    public ModelAndView vocaCard(int dirNum) {

        mav = new ModelAndView();

        List<VocaDTO> voca = new ArrayList<>();
        List<VocaCardDTO> vocaCardList = new ArrayList<>();

        voca = vdao.vocaView(dirNum);

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
                    imgHtml = eUrl.get(eUrl.size()-5).html();

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


}
