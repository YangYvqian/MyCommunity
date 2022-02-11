package com.yyq.mycommunity.controller;

import com.yyq.mycommunity.dto.PaginationDTO;
import com.yyq.mycommunity.model.User;
import com.yyq.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/profile/{action}")
    public String index(@PathVariable(name = "action")  String action,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "2") Integer size,
                        HttpServletRequest request){

        // 获取用户信息,从request中拿
        User user =(User)request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO pagination = questionService.list(user.getId(),page, size);
            model.addAttribute("pagination",pagination);
        }else if ("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            PaginationDTO pagination = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination",pagination);
        }
        return "profile";
    }
}
