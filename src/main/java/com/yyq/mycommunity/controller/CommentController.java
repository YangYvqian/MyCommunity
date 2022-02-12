package com.yyq.mycommunity.controller;


import com.yyq.mycommunity.dto.CommentDTO;
import com.yyq.mycommunity.dto.ResultDTO;
import com.yyq.mycommunity.exception.CustomizeErrorCode;
import com.yyq.mycommunity.mapper.CommentMapper;
import com.yyq.mycommunity.model.Comment;
import com.yyq.mycommunity.model.User;
import com.yyq.mycommunity.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {



    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setComment(commentDTO.getComment());
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(1);
        commentService.insert(comment);

        Map<Object,Object> map = new HashMap<>();
        map.put("message", "success");


        return ResultDTO.okOf();


    }
}
