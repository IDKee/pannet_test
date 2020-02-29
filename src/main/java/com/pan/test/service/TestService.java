package com.pan.test.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.pan.common.mybatis.BaseService;
import com.pan.test.entity.Test;
import com.pan.test.mapper.TestMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
*
* @author : panjie
* @date : 2020/2/26
*/

@Service
public class TestService extends BaseService<Test> {

    @Resource
    private TestMapper testMapper;

    @Cacheable(value = "testCache")
    public List<Test> selectListAll(){
        return testMapper.selectListAll();
    }
}
