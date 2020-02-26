package com.pan.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pan.test.entity.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<Test> {
}
