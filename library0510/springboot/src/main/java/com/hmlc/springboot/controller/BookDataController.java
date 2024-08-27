package com.hmlc.springboot.controller;

import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.service.BookDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName BookDataService
 * @Description 书籍数据控制层传参
 * @Author lazyFox
 * @Date 2024/6/10 10:38
 * @Version V0.1
 */
@RestController
@RequestMapping("/home")
public class BookDataController {

	@Resource
	private BookDataService bookDataService;

	// 获取卡片数据
	@GetMapping("/booksCard")
	public List<Book> getRandomBooksData(@RequestParam Integer cardSize) {
		return bookDataService.randomGetDataByNumber(cardSize);
	}

	// 获取书籍数据
	@GetMapping("/books")
	public List<Book> getRandomBooksByType(@RequestParam Integer type, @RequestParam Integer number) {
		return bookDataService.getBooksByType(type,number);
	}


}

