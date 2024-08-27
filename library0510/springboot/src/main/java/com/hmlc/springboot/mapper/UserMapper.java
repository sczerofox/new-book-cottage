package com.hmlc.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmlc.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
