package com.hmlc.springboot.controller;

import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.service.AboutService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName AboutController
 * @Description 关于 控制层
 * @Author lazyFox
 * @Date 2024/6/26 20:15
 * @Version V0.1
 */
@RestController
public class AboutController {

	@Resource
	private AboutService aboutService;

	@GetMapping("/about")
	public Result getAboutInfo(@RequestParam Integer id){
		return aboutService.findAboutById(id);
	}
}

