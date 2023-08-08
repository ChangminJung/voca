package com.personal.voca.controller;

import com.personal.voca.dto.VocaDTO;
import com.personal.voca.service.VocaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class VocaController {

    ModelAndView mav = new ModelAndView();

    private final HttpSession session;
    private final VocaService vsvc;

//    @GetMapping("/")
//    public String main(){
//        return "test3";
//    }
@GetMapping("/test")
public String test3(){
    return "tenQuiz";
}



    @GetMapping("/")
    public String main(){
        return "home";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/vocaInput")
    public String vocaInput(){
        return "vocaInput";
    }

    @GetMapping("/vocaCardQuiz")
    public ModelAndView vocaCardQuiz(@RequestParam int dirNum, @RequestParam int quizLv, @RequestParam int type){
        return vsvc.vocaCardQuiz(dirNum, quizLv, type);
    }

    @GetMapping("/vocaSubmit")
    public ModelAndView vocaSubmit(@RequestParam String tbName, @RequestParam String input){
        return vsvc.vocaSubmit(tbName, input);
    }

    @GetMapping("/vocaTable")
    public ModelAndView vocaTable(){
        return vsvc.vocaTable();
    }

    @GetMapping("/vocaView")
    public ModelAndView vocaView(@RequestParam int dirNum){
        return vsvc.vocaView(dirNum);
    }

    @PostMapping("/checkSave")
    public @ResponseBody List<List<VocaDTO>> checkSave(@RequestParam String input, @RequestParam int dirNum){
        return vsvc.checkSave(input, dirNum);
    }

    @GetMapping("/vocaTest")
    public ModelAndView vocaTest(@RequestParam int dirNum, @RequestParam int level, @RequestParam int type){
        return vsvc.vocaTest(dirNum, level, type);
    }

    @GetMapping("/vocaCard")
    public ModelAndView vocaCard(@RequestParam int dirNum, @RequestParam int type){
        return vsvc.vocaCard(dirNum, type);
    }

    @GetMapping("/vocaTenQuiz")
    public ModelAndView vocaTenQuiz(@RequestParam int dirNum, @RequestParam int type){
        return vsvc.vocaTenQuiz(dirNum, type);
    }

    @GetMapping("/vocaModiForm")
    public ModelAndView vocaModiForm(@RequestParam int dirNum){
        return vsvc.vocaModiForm(dirNum);
    }

    @GetMapping("/vocaModify")
    public ModelAndView vocaModify(@RequestParam String tbName, @RequestParam int dirNum, @RequestParam String input){
        return vsvc.vocaModify(tbName, dirNum, input);
    }

    @PostMapping("/imgSearch")
    public @ResponseBody String imgSearch(@RequestParam String input) {

        return vsvc.imgSearch(input);
    }

    @PostMapping("/searching")
    public @ResponseBody List<VocaDTO> searching(@RequestParam int dirNum) {

        return vsvc.searching(dirNum);
    }

    @PostMapping("/dirNumCheck")
    public @ResponseBody String dirNumCheck() {
        return vsvc.dirNumCheck();
    }

    @GetMapping("/imgModiform")
    public ModelAndView imgModiform(@RequestParam int voNum){
        return vsvc.imgModiform(voNum);
    }


    @PostMapping("/imgModify")
    public @ResponseBody List<Object> imgModify(@RequestParam int voNum){
        return vsvc.imgModify(voNum);
    }

    @PostMapping("/imgPick")
    public @ResponseBody String imgPick(@RequestParam int voNum, @RequestParam int imgLv, @RequestParam String imgUrl) {
        return vsvc.imgPick(voNum,imgLv,imgUrl);
    }

    @GetMapping("/quizResult")
    public ModelAndView quizResult(@RequestParam int dirNum, @RequestParam String input){
        return vsvc.quizResult(dirNum, input);
    }

    @PostMapping("/randomVoca")
    public @ResponseBody String randomVoca() {

        return vsvc.randomVoca();
    }




    @GetMapping("/charts-apexcharts")
    public String charts_apexcharts(){
        return "charts-apexcharts";
    }

    @GetMapping("/charts-chartjs")
    public String charts_chartjs(){
        return "charts-chartjs";
    }

    @GetMapping("/charts-echarts")
    public String charts_echarts(){
        return "charts-echarts";
    }

    @GetMapping("/components-accordion")
    public String components_accordion(){
        return "components-accordion";
    }

    @GetMapping("/components-alerts")
    public String components_alerts(){
        return "components-alerts";
    }

    @GetMapping("/components-badges")
    public String components_badges(){
        return "components-badges";
    }

    @GetMapping("/components-breadcrumbs")
    public String components_breadcrumbs(){
        return "components-breadcrumbs";
    }

    @GetMapping("/components-buttons")
    public String components_buttons(){
        return "components-buttons";
    }

    @GetMapping("/components-cards")
    public String components_cards(){
        return "components-cards";
    }

    @GetMapping("/components-carousel")
    public String components_carousel(){
        return "components-carousel";
    }

    @GetMapping("/components-list-group")
    public String components_list_group(){
        return "components-list-group";
    }

    @GetMapping("/components-modal")
    public String components_modal(){
        return "components-modal";
    }

    @GetMapping("/components-pagination")
    public String components_pagination(){
        return "components-pagination";
    }

    @GetMapping("/components-progress")
    public String components_progress(){
        return "components-progress";
    }

    @GetMapping("/components-spinners")
    public String components_spinners(){
        return "components-spinners";
    }

    @GetMapping("/components-tabs")
    public String components_tabs(){
        return "components-tabs";
    }

    @GetMapping("/components-tooltips")
    public String components_tooltips(){
        return "components-tooltips";
    }

    @GetMapping("/forms-editors")
    public String forms_editors(){
        return "forms-editors";
    }

    @GetMapping("/forms-elements")
    public String forms_elements(){
        return "forms-elements";
    }

    @GetMapping("/forms-layouts")
    public String forms_layouts(){
        return "forms-layouts";
    }

    @GetMapping("/forms-validation")
    public String forms_validation(){
        return "forms-validation";
    }

    @GetMapping("/icons-bootstrap")
    public String icons_bootstrap(){
        return "icons-bootstrap";
    }

    @GetMapping("/icons-boxicons")
    public String icons_boxicons(){
        return "icons-boxicons";
    }

    @GetMapping("/icons-remix")
    public String icons_remix(){
        return "icons-remix";
    }

    @GetMapping("/pages-blank")
    public String pages_blank(){
        return "pages-blank";
    }

    @GetMapping("/pages-contact")
    public String pages_contact(){
        return "pages-contact";
    }

    @GetMapping("/pages-error-404")
    public String pages_error_404(){
        return "pages-error-404";
    }

    @GetMapping("/pages-faq")
    public String pages_faq(){
        return "pages-faq";
    }

    @GetMapping("/pages-login")
    public String pages_login(){
        return "pages-login";
    }

    @GetMapping("/pages-register")
    public String pages_register(){
        return "pages-register";
    }

    @GetMapping("/tables-data")
    public String tables_data(){
        return "tables-data";
    }

    @GetMapping("/tables-general")
    public String tables_general(){
        return "tables-general";
    }

    @GetMapping("/users-profile")
    public String users_profile(){
        return "users-profile";
    }



}
