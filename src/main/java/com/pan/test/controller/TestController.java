package com.pan.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pan.test.entity.Test;
import com.pan.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
*
* @author : panjie
* @date : 2020/2/26
*/

@Controller
public class TestController {

    @Autowired
    private HttpServletRequest request;
    @Resource
    private TestService testService;

    @GetMapping("test")
    public String test(){
        LambdaQueryWrapper<Test> wrapper = new LambdaQueryWrapper<>();
        List list = testService.selectList(wrapper);
        request.setAttribute("list", list);
        return "test/test";
    }
}
