package com.hmlc.springboot.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName checkToken
 * @Description 验证token
 * @Author lazyFox
 * @Date 2024/6/6 23:33
 * @Version V0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckToken {

	private String username;
	private String key;
	private String token;
}

