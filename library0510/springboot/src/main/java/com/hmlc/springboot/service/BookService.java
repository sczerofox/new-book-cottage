package com.hmlc.springboot.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.Author;
import com.hmlc.springboot.entity.Book;
import com.hmlc.springboot.dto.common.PageBean;
import com.hmlc.springboot.mapper.BookMapper;
import com.hmlc.springboot.util.ApiUtil;
import com.hmlc.springboot.util.FilesUtils;
import com.hmlc.springboot.util.StringUtils;
import lombok.Lombok;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @Resource
    private BookMapper bookMapper;
    @Resource
    private PrivateService privateService;
    @Resource
    private AuthorService authorService;

    //  根据书名获取书籍信息
    public Result getBookByBookName(String bookName){
        Result result = new Result();
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("bookname",bookName);
        if(bookMapper.selectOne(wrapper) != null){
            result.setCode(1);
            result.setData(bookMapper.selectOne(wrapper));
            result.setMessage("ok");
            return result;
        }
        return result;
    }

    //  根据书名获取书籍信息
    public Book getBookByBookname(String bookname){
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("bookname",bookname);
        return bookMapper.selectOne(wrapper);
    }


    // 查询全部数据
    public List<Book> findAllBooks() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByDesc("update_time");
        wrapper.eq("is_del", 0);
        return bookMapper.selectList(wrapper);
    }

    //  根据 idList 查询书籍数据
    public List<Book> queryBooksByIdList(String[] idList){
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.in("id",idList);
        wrapper.eq("is_del",0);
        return bookMapper.selectList(wrapper);
    }

    //  根据 idList 查询书籍数据 (历史记录)
    public List<Book> queryBooksByIdListAndDesc(String[] idList){
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.in("id",idList);
        wrapper.orderByDesc("update_time");
        wrapper.eq("is_del",0);
        return bookMapper.selectList(wrapper);
    }


    // 分页查询全部数据
    public PageBean findAllBooksByPage(Integer page, Integer pageSize) {
        // 首先设置好分页参数
        PageHelper.startPage(page, pageSize);

        // 根据分页条件，从拿到的全部数据中去分页
        return ApiUtil.usePageBeanByPages(page,pageSize,findAllBooks());
    }

    // 按照 book name 进行模糊查询并分页
    public PageBean findAllBooksByBookname(Integer page, Integer pageSize, String bookname) {
        // 首先设置好分页参数
        PageHelper.startPage(page, pageSize);

        // 把模糊搜索条件构建好
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.like("bookname", bookname);
        queryWrapper.orderByDesc("update_time");
        queryWrapper.eq("is_del",0);

        // 根据分页条件，从拿到的全部数据中去分页
        return ApiUtil.usePageBeanByPages(page,pageSize,bookMapper.selectList(queryWrapper));
    }

    // 按照 id 进行数据恢复查询并分页
    public PageBean findAllDeleteBooksById(Integer page, Integer pageSize) {
        // 首先设置好分页参数
        PageHelper.startPage(page, pageSize);

        // 把模糊搜索条件构建好
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");
        queryWrapper.eq("is_del",1);

        // 根据分页条件，从拿到的全部数据中去分页
        return ApiUtil.usePageBeanByPages(page,pageSize,bookMapper.selectList(queryWrapper));
    }

    // 删除图书信息
    public Integer deleteBook(Book book) {
        book.setIsDel(1);
        return bookMapper.updateById(book);
    }

    // 恢复图书信息
    public Integer recoverBook(Book book) {
        book.setIsDel(0);
        return bookMapper.updateById(book);
    }

    // 编辑图书信息
    public Integer updateBook(Book book) {
        book.setUpdateTime(LocalDateTime.now());
        return bookMapper.updateById(book);
    }

    // 新增图书信息
    public Integer insertBook(Book book) {
        return bookMapper.insert(book);
    }



    //  作家新发布书籍
    public Result insertBook(Book book, MultipartFile file){
        Result result = new Result();
        Map<String,Object> map = new HashMap<>();

        String name = book.getAuthor();
        String bookname = book.getBookname();

        Boolean upAu = privateService.updatePrivateByIsAuthorInfo(name);    //  修改个人信息
        Integer inBo = bookMapper.insert(book);    //  注册书籍
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("bookname",bookname).orderByDesc("update_time");
        Integer boId = bookMapper.selectOne(wrapper).getId();    //  获取书籍的id
        Boolean adWo = authorService.addWorkInfo(name,boId);    //  添加作家表的

        map.put("fileName",book.getWebUrl());
        map.put("name",bookname);
        map.put("updateAuthor",upAu);
        map.put("insertBook",inBo);
        map.put("getBook",boId);
        map.put("addWork",adWo);

        if(inBo > 0){
            result.setCode(1);
            result.setData(map);
            result.setMessage("上传成功");
        }
        return result;
    }


    public Result updateHeaderUrl(String bookname, MultipartFile file){
        Result result = new Result();
        Map<String,Object> map = new HashMap<>();
        if(bookname.isEmpty()){
            result.setCode(0);
            result.setMessage("无法读取书名");
            map.put("isUpdate",false);
            map.put("filePath","");
            result.setData(map);
            return result;
        }

        //  获取文件
        String newFilePath = FilesUtils.filesUpload(file,2,null);    //  修改新的
        String oldFilePath = getBookByBookname(bookname).getWebUrl();    //  删除老的
        Boolean delete = false;

        if(oldFilePath.length() > 36){
            UpdateWrapper<Book> wrapper = new UpdateWrapper<>();
            wrapper.eq("bookname",bookname).set("web_url",newFilePath);
            bookMapper.update(null,wrapper);
        }

        //  修改 private 数据表
        UpdateWrapper<Book> wrapper = new UpdateWrapper<>();
        wrapper.eq("bookname",bookname).set("web_url",newFilePath);
        int i = bookMapper.update(null,wrapper);
        if(oldFilePath.isEmpty() || oldFilePath == null){}else {
            delete = FilesUtils.deleteFileByFilename(oldFilePath);
        }
        if(i > 0){
            result.setCode(1);
            result.setMessage("头像修改成功");
            result.setData(true);
            map.put("isUpdate",true);
            map.put("filePath",newFilePath);
            map.put("delete",delete);
            result.setData(map);
            return result;
        }
        return result;
    }
}
