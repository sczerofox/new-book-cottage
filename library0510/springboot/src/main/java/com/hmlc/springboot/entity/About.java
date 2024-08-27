package com.hmlc.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName About
 * @Description about 类
 * @Author lazyFox
 * @Date 2024/6/26 20:02
 * @Version V0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class About {

	@TableId(type = IdType.AUTO)
	private Integer abId;
	private String logo;
	private String name;
	private String version;
	private String lang;
	private String uuid;
	private String companyName;
	private String text1;
	private String iconImg;
	private String copyright;
	private String date;
	private String company;
	private String text2;

}

