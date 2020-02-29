package com.pan.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pan.test.entity.Test;
import com.pan.test.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*
* @author : panjie
* @date : 2020/2/29
*/

@RestController
@Api(tags = "pannet网站接口")
public class ApiPannetController {

    @Autowired
    private TestService testService;

    @GetMapping(value = "/getTestByTitle")
    @ApiOperation("根据title查询Test的接口")
    @ApiImplicitParam(name = "title", value = "用户id", required = false)
    public List<Test> getTestByTitle(String title){
        LambdaQueryWrapper<Test> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Test::getTitle, title);
        List<Test> list = testService.selectList(queryWrapper);
        return list;
    }
}
