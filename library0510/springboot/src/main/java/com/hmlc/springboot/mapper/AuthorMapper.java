package com.hmlc.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmlc.springboot.entity.Author;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthorMapper extends BaseMapper<Author>{

}
