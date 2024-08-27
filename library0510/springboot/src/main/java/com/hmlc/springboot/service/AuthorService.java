package com.hmlc.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.Author;
import com.hmlc.springboot.entity.Private;
import com.hmlc.springboot.mapper.AuthorMapper;
import com.hmlc.springboot.util.FilesUtils;
import com.hmlc.springboot.util.NumberUtil;
import com.hmlc.springboot.util.StringUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;

/**
 * @ClassName AuthorSerice
 * @Description 用户作家信息逻辑与业务层
 * @Author lazyFox
 * @Date 2024/6/11 0:33
 * @Version V0.1
 */
@Service
public class AuthorService {

	@Resource
	private AuthorMapper authorMapper;

	public Author getAuthorByName(String name){
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("name",name);
		return authorMapper.selectOne(wrapper);
	}

	//  author 是否有该用户名
	public Boolean isAuthorExistByName(String name){
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("name",name);
		if(authorMapper.selectOne(wrapper) != null){
			return true;
		}
		return false;
	}

	//  注册 作家表
	public Boolean registerAuthorByName(String name){
		Author author = new Author(null,name,null,0,0,2000,null,null,null);
		if(!isAuthorExistByName(name)){
			Integer isReg = authorMapper.insert(author);
			if (isReg > 0){
				randomAddAuthorExperience(name,120,100);
				return true;
			}
		}
		randomAddAuthorExperience(name,120,100);
		return false;
	}


	//  为作家添加 作品信息
	public Boolean addWorkInfo(String name, Integer id){
		UpdateWrapper<Author> wrapper = new UpdateWrapper<>();
		Author author = getAuthorByName(name);
		if(StringUtils.isEmpty(author.getWork())){
			wrapper.eq("name",name).set("work",id);
		}else {
			String oldWork = author.getWork();
			String newWork = oldWork+","+id;
			wrapper.eq("name",name).set("work",newWork);
		}
		if(authorMapper.update(null,wrapper) > 0){
			return true;
		}
		return false;
	}

	public Boolean randomAddAuthorExperience(String name,Integer max,Integer min){

		Integer oldGrade = getAuthorByName(name).getGrade();
		Integer oldExperience = getAuthorByName(name).getExperience();
		Integer experienceMax = getAuthorByName(name).getMax();

		Integer newGrade = 0;
		Integer randomExperience = NumberUtil.randomNumberByMaxAndMin(max,min);
		Integer newExperience = oldExperience + randomExperience;

		UpdateWrapper<Author> wrapper = new UpdateWrapper<>();

		if(newExperience >= experienceMax){
			newExperience = oldExperience % experienceMax;
			newGrade = oldExperience / experienceMax + oldGrade;
			wrapper.eq("name",name).set("grade",newGrade).set("experience",newExperience);
		}else {
			wrapper.eq("name",name).set("experience",newExperience);
		}

		Integer i = authorMapper.update(null,wrapper);
		if(i>0){
			return true;
		}
		return false;
	}

	public Result updateHeaderUrl(String name, MultipartFile file){
		Result result = new Result();
		Map<String,Object> map = new HashMap<>();
		if(name.isEmpty()){
			result.setCode(0);
			result.setMessage("无法读取用户名");
			map.put("isUpdate",false);
			map.put("filePath","");
			result.setData(map);
			return result;
		}
		//  获取文件
		String newFilePath = FilesUtils.filesUpload(file,2,null);    //  修改新的
		String oldFilePath = getAuthorByName(name).getHeaderUrl();    //  删除老的
		Boolean delete = false;

		//  修改 private 数据表
		UpdateWrapper<Author> wrapper = new UpdateWrapper<>();
		wrapper.eq("name",name).set("header_url",newFilePath);
		int i = authorMapper.update(null,wrapper);
		if(oldFilePath == null || oldFilePath.isEmpty()){}else {
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

