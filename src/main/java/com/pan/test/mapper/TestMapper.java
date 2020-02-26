package com.pan.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pan.test.entity.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** 
* 
* @author : panjie 
* @date : 2020/2/26
*/


@Mapper
public interface TestMapper extends BaseMapper<Test> {

    List<Test> selectListAll();
}
