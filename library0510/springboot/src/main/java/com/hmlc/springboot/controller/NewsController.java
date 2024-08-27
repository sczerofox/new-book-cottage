package com.hmlc.springboot.controller;

import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.entity.News;
import com.hmlc.springboot.service.BookDataService;
import com.hmlc.springboot.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName NewsConteoller
 * @Description 新闻控制层接口
 * @Author lazyFox
 * @Date 2024/6/10 10:39
 * @Version V0.1
 */
@RestController
@RequestMapping("/news")
public class NewsController {

	@Resource
	private NewsService newsService;

	// 构建首页接口，查询全部数据
	@GetMapping("/newsCard")
	public List<News> getRandomNewsCard(@RequestParam Integer newsSize) {
		return newsService.randomGetNewsByNumber(newsSize);
	}

	// 构建首页接口，查询全部数据
	@GetMapping("/news")
	public List<News> getRandomNews(@RequestParam Integer newsSize) {
		return newsService.GetNewsByNumber(newsSize);
	}

}

