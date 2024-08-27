package com.hmlc.springboot.controller;

import com.hmlc.springboot.entity.Author;
import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.service.AuthorService;
import com.hmlc.springboot.service.BookService;
import com.hmlc.springboot.service.PrivateService;
import com.hmlc.springboot.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AuthorController
 * @Description 用户作家信息控制层接口
 * @Author lazyFox
 * @Date 2024/6/11 0:36
 * @Version V0.1
 */
@RestController
public class AuthorController {

	@Resource
	private AuthorService authorService;
	@Resource
	private BookService bookService;
	@Resource
	private PrivateService privateService;

	@GetMapping("/author")
	public Author getAuthorInfo(@RequestParam String name){
		Author author = new Author();
		if(authorService.isAuthorExistByName(name)){
			if(privateService.getPrivateByName(name).getIsAuthor().equals(0)){return author;}
			return  authorService.getAuthorByName(name);
		}
		return author;
	}

	@GetMapping("/aWork")
	public List<Book> getWorkInfo(@RequestParam String name){
		List<Book> books = new ArrayList<>();
		if(authorService.isAuthorExistByName(name)){
			if(privateService.getPrivateByName(name).getIsAuthor().equals(0)){return books;}
			return  bookService.queryBooksByIdList(StringUtils.toStringArray(authorService.getAuthorByName(name).getWork()));
		}
		return books;
	}

	@GetMapping("/aStar")
	public List<Book> getStarInfo(@RequestParam String name){
		List<Book> books = new ArrayList<>();
		if(authorService.isAuthorExistByName(name)){
			if(privateService.getPrivateByName(name).getIsAuthor().equals(0)){return books;}
			return  bookService.queryBooksByIdList(StringUtils.toStringArray(authorService.getAuthorByName(name).getStar()));
		}
		return books;
	}

	@GetMapping("/aHistory")
	public List<Book> getHistoryInfo(@RequestParam String name){
		List<Book> books = new ArrayList<>();
		if(authorService.isAuthorExistByName(name)){
			if(privateService.getPrivateByName(name).getIsAuthor().equals(0)){return books;}
			return  bookService.queryBooksByIdListAndDesc(StringUtils.toStringArray(authorService.getAuthorByName(name).getHistory()));
		}
		return books;
	}

}

