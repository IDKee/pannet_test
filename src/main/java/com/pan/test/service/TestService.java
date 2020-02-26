package com.pan.test.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.pan.test.entity.Test;
import com.pan.test.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    public List selectList(Wrapper<Test> queryWrapper){
        return testMapper.selectList(queryWrapper);
    }

    public Test selectById(Serializable id){
        return testMapper.selectById(id);
    }
}
