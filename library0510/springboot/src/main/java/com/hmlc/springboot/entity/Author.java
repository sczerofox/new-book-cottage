package com.hmlc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Author
 * @Description 用户作家信息表
 * @Author lazyFox
 * @Date 2024/6/11 0:26
 * @Version V0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

	@TableId(type = IdType.AUTO)
	private Integer auId;
	private String name;
	private String headerUrl;
	private Integer grade;
	private Integer experience;
	private Integer max;
	private String work;
	private String star;
	private String history;
}

