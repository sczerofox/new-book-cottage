package com.hmlc.springboot.controller;

import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.service.AuthorService;
import com.hmlc.springboot.service.BookService;
import com.hmlc.springboot.service.PrivateService;
import com.hmlc.springboot.util.FilesUtils;
import com.hmlc.springboot.util.TestUtils;
import com.hmlc.springboot.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;


/**
 * @ClassName UplodController
 * @Description 文件上传数据接收
 * @Author lazyFox
 * @Date 2024/6/8 0:13
 * @Version V0.1
 */
@RestController
public class UploadController {

	@Resource
	private PrivateService privateService;
	@Resource
	private BookService bookService;
	@Resource
	private AuthorService authorService;
	@Autowired
	private Environment env;

	/**
	 * @Author LazyFox
	 * @Description 文件上传
	 * type 返回类型
	 * 1 path
	 * 2 filename
	 * @Date 0:23 2024/6/8
	 */
	@PostMapping("/filesUpload")
	public String filesUpload(MultipartFile file, Integer type) {
		int port = env.getProperty("server.port", Integer.class);
		return FilesUtils.filesUpload(file,type,port);
	}

	@PostMapping("/filesDelete")
	public Boolean filesDelete(@RequestParam String filePath) {
		return FilesUtils.deleteFileByPath(filePath);
	}

	@PostMapping("/privateUpload")
	public Result privateHeaderUpload(@RequestParam String name, MultipartFile file){
		return privateService.updateHeaderUrl(name,file);
	}

	@PostMapping("/authorUpload")
	public Result authorHeaderUpload(@RequestParam String name, MultipartFile file){
		return authorService.updateHeaderUrl(name, file);
	}

	@PostMapping("/coverUpload")
	public Result coverHeaderUpload(@RequestParam String name, MultipartFile file){
		return bookService.updateHeaderUrl(name, file);
	}

	@PostMapping("/bookUpload")
	public Result uploadBookCover(@RequestParam String bookname,
								  @RequestParam String author,
								  @RequestParam BigDecimal price,
								  @RequestParam Integer count,
								  @RequestParam String type,
								  MultipartFile file){
		String date = TimeUtil.getCurrentTime();
		String image= FilesUtils.filesUpload(file,2,null);
		Book book = new Book(null,bookname,author,price,date,count,null,null,null,type,null,image);
		return bookService.insertBook(book,file);
	}


}

