package com.yyq.mycommunity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 为什么要定义这个PaginationDTO呢，
 * 因为现在往前端返回的不光有数据，还要是分页的数据，所以把页封装
 * 这个DTO即有需要的数据也有页的一些信息，
 * 这也说明了service的作用，就是对信息进行组合，然后获取符合自己需求的数据
 *
 * QuestionDTO组合了（封装）Question 和 user两个实体类
 * PaginationDTO 组合了（封装）QuestionDTO 和 页信息
 * **/

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions; //获取question表的问题列表
    private boolean showPrevious;   // 前一页 <
    private boolean showFirstPage;  // 第一页 <<
    private boolean showNext;       // 下一页 >
    private boolean showEndPage;    // 最后一页 >>
    private Integer page;           // 当前页
    private List<Integer> pages = new ArrayList<>();  //展示的列表页
    private Integer totalPage;    // 总页数

    public void setPagination(Integer totalCount, Integer page, Integer size) {


        /*
        * 分页逻辑和需要注意的。
        * 1.当点击第1页的时候，上一页不显示 showPrevious
        * 2.当点击最后一页的时候，下一页不显示 showNext
        * 3.如果当前页列表包含第一页，不展示第一页 showFirstPage
        * 4.如果当前页列表包含最后一页，不展示最后一页 showEndPage
        * 5.当前页的页列表，分当前页的前一部分，和后一部分，需要一个计数控制，目的是为了控制当前页前面显示多少，后面同理。
        *
        * 需要注意的是页码越界问题，因为页码越界在前端传的值越界了，controller中获取的页码是越界的参数，所以需要处理一下
        */



        // 计算总页数  totalCount / size
//        Integer totalPage = 0;
         if (totalCount % size == 0){
             totalPage = totalCount / size;
         }else{
             totalPage = (totalCount / size) + 1;
         }
        // 页码越界处理问题
        if (page<1) page=1;
        if (page > totalPage) page = totalPage;

        this.page = page;
        // 获取当前页的页列表
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
