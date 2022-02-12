package com.yyq.mycommunity.controller;

import com.yyq.mycommunity.dto.PaginationDTO;
import com.yyq.mycommunity.dto.QuestionDTO;
import com.yyq.mycommunity.mapper.UserMapper;
import com.yyq.mycommunity.model.User;
import com.yyq.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {


//    private UserMapper userMapper;
    private QuestionService questionService;

//    @Autowired
//    public void setUserMapper(UserMapper userMapper) {
//        this.userMapper = userMapper;
//    }
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    //    写一个从cookie中获取token的方法
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,  // 从前端页面传当前点击的页码
                        @RequestParam(name = "size", defaultValue = "8") Integer size){  //设置每页展示多少size
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length != 0)
//            for (Cookie cookie : cookies){
//                if (cookie.getName().equals("token")){
//                    String token = cookie.getValue();
//                    User user = userMapper.findByToken(token);
//    //                如果user不为null的时候，把user信息写到session中，让页面展示
//    //                访问首页的时候，循环去看所有的cookie，找到cookie等于token的cookie，
//    //                拿到这个cookie去数据库中查，是不是数据库中有这条记录，如果有，把这个user信息放到session里
//                    if (user != null){
//                        request.getSession().setAttribute("user", user);
//                    }
//                    break;
//                }
//            }
        // page size 传给service层做分页处理
//        获取到的数据是封装了QuestionDTO 和 页信息 的数据
//        并把信息列表和页信息传给前端
        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination",pagination);

        return "index";
    }

}
