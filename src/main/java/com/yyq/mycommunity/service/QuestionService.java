package com.yyq.mycommunity.service;

import com.yyq.mycommunity.dto.PaginationDTO;
import com.yyq.mycommunity.dto.QuestionDTO;
import com.yyq.mycommunity.exception.CustomizeErrorCode;
import com.yyq.mycommunity.exception.CustomizeException;
import com.yyq.mycommunity.mapper.QuestionMapper;
import com.yyq.mycommunity.mapper.UserMapper;
import com.yyq.mycommunity.model.Question;
import com.yyq.mycommunity.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 *      为什么要创建这个QuestionService？
 *      因为想要需要的信息在两个表中存在，question表和user表，所以需要一个中间层即service层，来组合这两个表中的信息
 *      service对应数据传输DTO
 *
 *      @param
 *      @page：前端点击的当前也
 *      @size 每页显示的数量
 *      @offset 每页从第几条开始显示，页偏移量
 *      @totalCount 总问题数
 **/
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    private UserMapper userMapper;

//    @Autowired
//    public void setQuestionMapper(QuestionMapper questionMapper) {
//        this.questionMapper = questionMapper;
//    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /****************************************************************************/
//  分页列表展示，从Controller层接收两个page size参数，对分页进行处理
//    返回的数据是封装了QuestionDTO 和 页信息 的数据
    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        // 获取question表中问题的总数
        Integer totalCount = questionMapper.count();
        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = (totalCount / size) + 1;
        }


        // 此代码有两处判断页码越界的问题
        //有两处判断的原因是有两个地方需要做越界处理
        // 1.数据库查询时，page是否越界
        // 2.前端页面展示的时候，page页表中不能显示越界信息
        //当前是1的情况
        // 能不能只出现一次判断越界呢？
        if (page > totalPage) page = totalPage;
        if (page<1) page=1;


        // 把totalCount, page, size 传到paginationDTO，进行页信息计算
        paginationDTO.setPagination(totalPage, page);
        // 计算页面偏移量，也就是算出每页从第几条开始
        Integer offset = size * (page - 1);
        // 获取问题列表
        // 传递给查询question表的select语句的limit的两个参数，
        // select * from question limit offset size
        List<Question> questions = questionMapper.list(offset,size);


//        把问题和用户添加到questionDTOList列表中，进行组合
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user= userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

//        把封装了Question 和 User的questionDTO，传给paginationDTO的questions
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    /****************************************************************************/
    public PaginationDTO list(Integer userId,Integer cpage, Integer size) {


        PaginationDTO paginationDTO = new PaginationDTO();
        // 获取question表中问题的总数
        Integer totalCount = questionMapper.countByUserId(userId);
        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else{
            totalPage = (totalCount / size) + 1;
        }


        // 此代码有两处判断页码越界的问题
        //有两处判断的原因是有两个地方需要做越界处理
        // 1.数据库查询时，page是否越界
        // 2.前端页面展示的时候，page页表中不能显示越界信息
        //当前是1的情况
        // 能不能只出现一次判断越界呢？


        if (cpage > totalPage) cpage = totalPage;
        if (cpage < 1) {
            cpage = 1;
        }

        // 把totalCount, page, size 传到paginationDTO，进行页信息计算
        paginationDTO.setPagination(totalPage, cpage);

        // 计算页面偏移量，也就是算出每页从第几条开始
        Integer offset = size * (cpage - 1);
        // 获取问题列表
        // 传递给查询question表的select语句的limit的两个参数，
        // select * from question limit offset size
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);

//        把问题和用户添加到questionDTOList列表中，进行组合
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user= userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

//        把封装了Question 和 User的questionDTO，传给paginationDTO的questions
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer questionId) {
        Question question =  questionMapper.getQuestionByQuestionId(questionId);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user= userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;

    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.creation(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            int update = questionMapper.updateQuestion(question);
            if (update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id){
        Question questionByQuestionId = questionMapper.getQuestionByQuestionId(id);
        questionByQuestionId.setViewCount(questionByQuestionId.getViewCount()+1);
        questionMapper.updateQuestionViewCount(questionByQuestionId);


    }
}
