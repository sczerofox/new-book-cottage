package com.hmlc.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmlc.springboot.entity.About;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AboutMapper extends BaseMapper<About> {
}
