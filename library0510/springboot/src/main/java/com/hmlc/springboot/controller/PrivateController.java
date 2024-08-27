package com.hmlc.springboot.controller;

import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.entity.Private;
import com.hmlc.springboot.entity.User;
import com.hmlc.springboot.service.BookService;
import com.hmlc.springboot.service.PrivateService;
import com.hmlc.springboot.service.UserService;
import com.hmlc.springboot.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PrivateController
 * @Description 用户个人信息控制层接口
 * @Author lazyFox
 * @Date 2024/6/11 0:35
 * @Version V0.1
 */
@RestController
public class PrivateController {

	@Resource
	private PrivateService privateService;
	@Resource
	private UserService userService;
	@Resource
	private BookService bookService;

	@GetMapping("/private")
	public Private getPrivateInfo(@RequestParam String name){
//		Private pri = new Private();
//		if(privateService.isPrivateExistByName(name)){
//			privateService.isImageExist(name);
//			return  privateService.getPrivateByName(name);
//		}
//		return pri;
		return privateService.getPrivateByName(name);
	}

	@GetMapping("/userInfo")
	public User getUserInfo(@RequestParam String name){
		User user = new User();
		if(privateService.isPrivateExistByName(name)){
			user = userService.queryUserByUsername(name);
			user.setPassword("******");
			return  user;
		}
		return user;
	}

	@GetMapping("/isAuthor")
	public Boolean getIsAuthorInfo(@RequestParam String name){
		if(privateService.isPrivateExistByName(name)){
			if(privateService.getPrivateByName(name).getIsAuthor().equals(1)){
				return  true;
			}
		}
		return false;
	}

	@GetMapping("/admin")
	public Boolean getIsAdmin(@RequestParam String name){
		if(privateService.isPrivateExistByName(name)){
			if(privateService.getPrivateByName(name).getAdmin().equals(1)){
				return  true;
			}
		}
		return false;
	}

	@GetMapping("/pStar")
	public List<Book> getStarInfo(@RequestParam String name){
		List<Book> books = new ArrayList<>();
		if(privateService.isPrivateExistByName(name)){
			return  bookService.queryBooksByIdList(StringUtils.toStringArray(privateService.getPrivateByName(name).getStar()));
		}
		return books;
	}

	@GetMapping("/pHistory")
	public List<Book> getHistoryInfo(@RequestParam String name){
		List<Book> books = new ArrayList<>();
		if(privateService.isPrivateExistByName(name)){
			return  bookService.queryBooksByIdListAndDesc(StringUtils.toStringArray(privateService.getPrivateByName(name).getHistory()));
		}
		return books;
	}
}

