package com.hmlc.springboot.dto.about;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AboutImagesDto
 * @Description About 文件数据加密
 * @Author lazyFox
 * @Date 2024/6/26 21:25
 * @Version V0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AboutImagesDto {

	private String[] images;
}

