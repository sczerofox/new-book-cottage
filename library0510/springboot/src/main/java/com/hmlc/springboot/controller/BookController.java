package com.hmlc.springboot.controller;


import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.dto.common.PageBean;
import com.hmlc.springboot.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Resource
    private BookService bookService;

    //  根据书名获取一本书的接口
    @GetMapping("/bookOne")
    public Result getBookByBookName(@RequestParam String name){
        return bookService.getBookByBookName(name);
    }

    // 构建首页接口，查询全部数据
    @GetMapping("/home")
    public List<Book> getFillAllData() {
        return bookService.findAllBooks();
    }

    // 构建分页查询接口，查询分页数据
    @GetMapping("/pagination")
    public PageBean getFillAllDataByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return bookService.findAllBooksByPage(page, pageSize);
    }

    // 构建模糊查询接口，查询bookname的分页数据
    @GetMapping("/search")
    public PageBean getFillAllDataByObscureInfo(@RequestParam Integer page, @RequestParam Integer pageSize,
                             @RequestParam String bookname) {
        return bookService.findAllBooksByBookname(page, pageSize, bookname);
    }

    // 构建模糊查询接口，查询bookname的分页数据
    @GetMapping("/recoverData")
    public PageBean getFillAllDataByDeleteInfo(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return bookService.findAllDeleteBooksById(page, pageSize);
    }

    // 构建删除接口，删除图书信息
    @PostMapping("/remove")
    public Integer deleteBook(@RequestBody Book book) {
        return bookService.deleteBook(book);
    }

    // 构建删除接口，删除图书信息
    @PostMapping("/recover")
    public Integer recoverBook(@RequestBody Book book) {
        return bookService.recoverBook(book);
    }

    // 构建编辑接口，编辑图书信息
    @PostMapping("/edit")
    public Integer updateBook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }

    // 构建新增接口，新增图书信息
    @PostMapping("/add")
    public Integer insertBook(@RequestBody Book book) {
        return bookService.insertBook(book);
    }
}
