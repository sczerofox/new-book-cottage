package com.hmlc.springboot.controller;

import com.hmlc.springboot.dto.common.CheckToken;
import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.User;
import com.hmlc.springboot.service.AuthService;
import com.hmlc.springboot.util.CaptchaV3Util;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName AuthController
 * @Description 实现登录注册的前后端数据传递
 * @Author lazyFox
 * @Date 2024/6/6 14:53
 * @Version V0.1
 */
@RestController
public class AuthController {

	@Resource
	private AuthService authService;

	//  登录
	@PostMapping("/login")
	public Result login(@RequestBody User user){
		return authService.login(user.getUsername(),user.getPassword());
	}

	//  退出登录
	@GetMapping("/out")
	public Result login(@RequestParam String username, @RequestParam String key){
		return authService.outLogin(username,key);
	}

	//  注册
	@PostMapping("/register")
	public Result register(@RequestBody User user){
		return authService.register(user.getUsername(),user.getPassword(),user.getPhone());
	}

	//  获取 验证码
	@GetMapping("/captcha")
	public String getCaptcha(@RequestParam String phone){
		return CaptchaV3Util.createCaptchaByTime(phone,1L);
	}

	//  验证 验证码
	@GetMapping("/checkCaptcha")
	public Result checkCaptcha(@RequestParam String phone,@RequestParam String captcha){
		return authService.checkCaptcha(phone,captcha);
	}

	//  验证 鉴权码
	@PostMapping("/checkToken")
	public Result checkToken(@RequestBody CheckToken checkToken){
		return authService.checkToken(checkToken.getKey(),checkToken.getToken());
	}

	@GetMapping("/getCardNum")
	public Result getCardNum(@RequestParam String name){
		return authService.generateNewCardNum(name);
	}

}

