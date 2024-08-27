package com.hmlc.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.entity.News;
import com.hmlc.springboot.mapper.NewsMapper;
import com.hmlc.springboot.util.NumberUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName NewsService
 * @Description 新闻逻辑和业务处理层
 * @Author lazyFox
 * @Date 2024/6/10 10:30
 * @Version V0.1
 */
@Service
public class NewsService {

	@Resource
	private NewsMapper newsMapper;

	// 查询全部数据
	public List<News> findAllNews() {
		QueryWrapper wrapper = new QueryWrapper();
		return newsMapper.selectList(wrapper);
	}

	// 根据id查询单条数据
	public News findByOneNews(Integer id) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("news_id",id);
		return newsMapper.selectOne(wrapper);
	}

	// 查询全部数据选出Id最大值
	public Integer findAllNewsByIdMax() {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.orderByDesc("news_id");
		List<News> news = newsMapper.selectList(wrapper);
		return news.get(0).getNewsId();
	}


	//  随机获取书籍信息
	public List<News> randomGetNewsByNumber(Integer number) {
		List<News> news = new ArrayList<>();
		Set<Integer> uniqueIds = new HashSet<>();

		while (uniqueIds.size() < number) {
			int randomId = NumberUtil.randomNumberByInteger(findAllNewsByIdMax());
			uniqueIds.add(randomId);
		}

		QueryWrapper<News> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("news_id", uniqueIds);
		List<News> selectedNews = newsMapper.selectList(queryWrapper);

		news.addAll(selectedNews);

		return news;
	}

	//  随机获取书籍信息
	public List<News> GetNewsByNumber(Integer number) {
		Set<Integer> uniqueIds = new HashSet<>();

		while (uniqueIds.size() < number) {
			int randomId = NumberUtil.randomNumberByInteger(findAllNewsByIdMax());
			uniqueIds.add(randomId);
		}

		QueryWrapper<News> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("news_id", uniqueIds);
		return newsMapper.selectList(queryWrapper);
	}
}

