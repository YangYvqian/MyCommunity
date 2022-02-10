package com.yyq.mycommunity.controller;

import com.yyq.mycommunity.mapper.UserMapper;
import com.yyq.mycommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;


//    写一个从cookie中获取token的方法
    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
//                如果user不为null的时候，把user信息写到session中，让页面展示
//                访问首页的时候，循环去看所有的cookie，找到cookie等于token的cookie，
//                拿到这个cookie去数据库中查，是不是数据库中有这条记录，如果有，把这个user信息放到session里
                if (user != null){
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        return "index";
    }

}
