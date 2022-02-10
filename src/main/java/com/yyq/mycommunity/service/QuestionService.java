package com.yyq.mycommunity.service;

import com.yyq.mycommunity.dto.QuestionDTO;
import com.yyq.mycommunity.mapper.QuestionMapper;
import com.yyq.mycommunity.mapper.UserMapper;
import com.yyq.mycommunity.model.Question;
import com.yyq.mycommunity.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    private QuestionMapper questionMapper;
    private UserMapper userMapper;

    @Autowired
    public void setQuestionMapper(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<QuestionDTO> list() {

        List<Question> questions = questionMapper.list();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user= userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }
}
