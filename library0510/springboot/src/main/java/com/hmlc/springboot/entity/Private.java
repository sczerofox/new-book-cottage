package com.hmlc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Private
 * @Description 用户个人信息表
 * @Author lazyFox
 * @Date 2024/6/11 0:22
 * @Version V0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Private {

	@TableId(type = IdType.AUTO)
	private Integer prId;
	private String name;
	private String headerUrl;
	private Integer grade;
	private Integer experience;
	private Integer max;
	private String star;
	private String history;
	private Integer isAuthor;
	private Integer admin;
}

