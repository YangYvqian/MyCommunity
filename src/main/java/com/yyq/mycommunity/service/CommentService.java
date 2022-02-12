package com.yyq.mycommunity.service;

import com.yyq.mycommunity.enums.CommentTypeEnum;
import com.yyq.mycommunity.exception.CustomizeErrorCode;
import com.yyq.mycommunity.exception.CustomizeException;
import com.yyq.mycommunity.mapper.CommentMapper;
import com.yyq.mycommunity.mapper.QuestionMapper;
import com.yyq.mycommunity.model.Comment;
import com.yyq.mycommunity.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.undo.CannotUndoException;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Transactional
    public void insert(Comment comment) {

        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            // 查看是否有当前parentId的回复没
            Comment dbComment = commentMapper.geCommentByParentId(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }else {
                commentMapper.insert(comment);
            }

        }else {
        //    回复问题
            Question question = questionMapper.getQuestionByQuestionId(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(question.getCommentCount()+1);
            questionMapper.intCommentCount(question);



        }





    }
}
