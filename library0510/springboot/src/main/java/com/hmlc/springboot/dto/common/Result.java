package com.hmlc.springboot.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Result
 * @Description 登录和注册时，后端要返回给前端的东西
 * @Author lazyFox
 * @Date 2024/6/6 14:46
 * @Version V0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

	//  返回的状态码 0 可以是登录成功 -1 可以是密码有误等等
	private Integer code;
	//  返回操作信息 “密码有误”
 	private String message;
	//  返回 Object 数据对象
	private Object data;
}

