package com.pan.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
*
* @author : panjie
* @date : 2020/2/26
*/

@Data
@TableName(value = "t_test")
public class Test {

    private int id;

    private String title;

    private String content;

    private String test;
}
