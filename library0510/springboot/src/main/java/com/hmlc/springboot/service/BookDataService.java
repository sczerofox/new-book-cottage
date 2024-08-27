package com.hmlc.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.mapper.BookMapper;
import com.hmlc.springboot.util.NumberUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName BookDataService
 * @Description 书籍数据业务逻辑层
 * @Author lazyFox
 * @Date 2024/6/10 10:29
 * @Version V0.1
 */
@Service
public class BookDataService {

	@Resource
	private BookMapper bookMapper;

	// 查询全部数据
	public List<Book> findAllBooksData() {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.orderByDesc("update_time");
		wrapper.eq("is_del", 0);
		return bookMapper.selectList(wrapper);
	}

	// 根据id查询单条数据
	public Book findByOneBooksData(Integer id) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("id",id);
		wrapper.eq("is_del", 0);
		return bookMapper.selectOne(wrapper);
	}

	// 查询全部数据选出Id最大值
	public Integer findAllBooksByIdMax() {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.orderByDesc("id");
		wrapper.eq("is_del", 0);
		List<Book> books = bookMapper.selectList(wrapper);
		return books.get(0).getId();
	}


	//  随机获取书籍信息
	public List<Book> randomGetDataByNumber(Integer number) {
		Set<Integer> uniqueIds = new HashSet<>();

		while (uniqueIds.size() < number) {
			int randomId = NumberUtil.randomNumberByInteger(findAllBooksByIdMax());
			uniqueIds.add(randomId);
		}

		QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id", uniqueIds).eq("is_del", 0);
		return bookMapper.selectList(queryWrapper);
	}

	//  依据类型获取书籍数据
	public List<Book> getBooksByType(Integer type, Integer number) {
		QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
		Set<Integer> uniqueIds = new HashSet<>();

		if (type == 1) {
			while (uniqueIds.size() < number) {
				int randomId = NumberUtil.randomNumberByInteger(findAllBooksByIdMax());
				uniqueIds.add(randomId);
			}
			queryWrapper.in("id", uniqueIds).eq("is_del", 0).orderByDesc("hot");
		} else {
			queryWrapper.eq("is_del", 0);
			switch (type) {
				case 2:
					queryWrapper.orderByDesc("update_time");
					break;
				case 3:
					queryWrapper.orderByDesc("is_star");
					break;
				default:
					return new ArrayList<>();
			}
		}
		queryWrapper.last("LIMIT " + number);
		return bookMapper.selectList(queryWrapper);
	}


}

