package com.hmlc.springboot.controller;

import com.hmlc.springboot.util.FilesUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName DownLinkController
 * @Description 图片下行 （本地存储）
 * @Author lazyFox
 * @Date 2024/6/15 13:22
 * @Version V0.1
 */
@RestController
public class DownLinkController {

	@GetMapping("/images/{filename:.+}")
	public ResponseEntity<Resource> getImgByFilename(@PathVariable String filename){
		return FilesUtils.getImageByFilename(filename);
	}

	@GetMapping("/image")
	public ResponseEntity<Resource> getImgByAllFilepath(@RequestParam String filepath){
		return FilesUtils.getImageByAllFilepath(filepath);
	}


}

