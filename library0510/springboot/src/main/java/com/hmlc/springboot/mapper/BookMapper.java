package com.hmlc.springboot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmlc.springboot.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
