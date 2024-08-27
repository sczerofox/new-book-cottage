package com.hmlc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName User
 * @Description 用户信息表
 * @Author lazyFox
 * @Date 2024/5/25 22:45
 * @Version V1.0
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@TableId(type = IdType.AUTO)
	private Integer userId;
	private String username;
	private String realName;
	private String gender;
	private String password;
	private String email;
	private String phone;
	private String cardNum;
	private Integer isDel;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	private Integer isLogin;
}
