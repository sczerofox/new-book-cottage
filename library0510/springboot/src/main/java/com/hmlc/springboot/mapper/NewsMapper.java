package com.hmlc.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmlc.springboot.entity.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
