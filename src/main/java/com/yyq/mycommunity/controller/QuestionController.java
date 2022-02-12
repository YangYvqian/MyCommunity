package com.yyq.mycommunity.controller;

import com.yyq.mycommunity.dto.QuestionDTO;
import com.yyq.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    private QuestionService questionService;
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/question/{QuestionId}")
    public String question(@PathVariable(name = "QuestionId") Integer QuestionId,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(QuestionId);
        model.addAttribute("question", questionDTO);
        return "question";
    }

}
