package com.hmlc.springboot.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hmlc.springboot.dto.common.PageBean;

import java.util.List;

/**
 * @ClassName ApiUtil
 * @Description 接口工具类
 * @Author lazyFox
 * @Date 2024/5/25 11:53
 * @Version V1.0
 */
public class ApiUtil {

	public static PageBean usePageBeanByPages(Integer page, Integer pageSize, List valueList) {
		// 首先设置好分页参数
		PageHelper.startPage(page, pageSize);

		// 根据分页条件，从拿到的全部数据中去分页
		Page pg = (Page) valueList;

		long total = pg.getTotal();
		List list = pg.getResult();

		PageBean pageBean = new PageBean();

		pageBean.setTotal(total);
		pageBean.setList(list);

		return pageBean;
	}
}

