package com.hmlc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName News
 * @Description 新闻表
 * @Author lazyFox
 * @Date 2024/6/10 10:25
 * @Version V0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {

	@TableId(type = IdType.AUTO)
	private Integer newsId;
	private String webUrl;
	private String text;
	private String cardUrl;
}

