package com.yyq.mycommunity.controller;

import com.yyq.mycommunity.dto.QuestionDTO;
import com.yyq.mycommunity.mapper.QuestionMapper;
import com.yyq.mycommunity.model.Question;
import com.yyq.mycommunity.model.User;
import com.yyq.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    private QuestionMapper questionMapper;
    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Autowired
    public void setQuestionMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }


    @GetMapping("/publish")
    public String publish(){
        return "publish";

    }

//    创建一个想发表的问题，并插入数据库
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam(value = "id",required = false) Integer id,
                            HttpServletRequest request,
                            Model model){
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
//        把前端获取到的信息展示到前端来判断是否有问题，以及所填的地方不能为空
        if (title == null || title == ""){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == ""){
            model.addAttribute("error", "问题不能为空");
            return "publish";
        }
        if (tag == null || tag == ""){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        // 获取用户信息,从request中的session拿
        User user =(User)request.getSession().getAttribute("user");
        if (user == null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());

        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String editPublish(@PathVariable(name = "id") Integer id,
                              Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }
}
