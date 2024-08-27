package com.hmlc.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.Author;
import com.hmlc.springboot.entity.Private;
import com.hmlc.springboot.mapper.PrivateMapper;
import com.hmlc.springboot.util.FilesUtils;
import com.hmlc.springboot.util.NumberUtil;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PrivateService
 * @Description 用户个人信息逻辑与业务层
 * @Author lazyFox
 * @Date 2024/6/11 0:31
 * @Version V0.1
 */

@Service
public class PrivateService {

	@Resource
	private PrivateMapper privateMapper;
	@Resource
	AuthorService authorService;

	//  判断是否是作家
	public Boolean isAuthor(String name){
		QueryWrapper<Private> wrapper = new QueryWrapper();
		wrapper.eq("name",name);
		Private pr = privateMapper.selectOne(wrapper);
		if(pr.getIsAuthor().equals(1)){    //  true 是作家
			return true;
		}
		return false;
	}

	//   修改 个人用户为作家 并且创建作家表
	public Boolean updatePrivateByIsAuthorInfo(String name){
		UpdateWrapper<Private> wrapper = new UpdateWrapper<>();
		wrapper.eq("name",name).set("is_author",1);
		Integer upPrInfo = privateMapper.update(null,wrapper);
		if(upPrInfo > 0){
			return authorService.registerAuthorByName(name);
		}
		return false;
	}

	public Private getPrivateByName(String name){
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("name",name);
		return privateMapper.selectOne(wrapper);
	}

	//  private 是否有该用户名
	public Boolean isPrivateExistByName(String name){
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("name",name);
		if(privateMapper.selectOne(wrapper) != null){
			return true;
		}
		return false;
	}

	public Boolean randomAddPrivateExperience(String name,Integer max,Integer min){

		Integer oldGrade = getPrivateByName(name).getGrade();
		Integer oldExperience = getPrivateByName(name).getExperience();
		Integer experienceMax = getPrivateByName(name).getMax();

		Integer newGrade = 0;
		Integer randomExperience = NumberUtil.randomNumberByMaxAndMin(max,min);
		Integer newExperience = oldExperience + randomExperience;

		UpdateWrapper<Private> wrapper = new UpdateWrapper<>();

		if(newExperience >= experienceMax){
			newExperience = oldExperience % experienceMax;
			newGrade = oldExperience / experienceMax + oldGrade;
			wrapper.eq("name",name).set("grade",newGrade).set("experience",newExperience);
		}else {
			wrapper.eq("name",name).set("experience",newExperience);
		}

		Integer i = privateMapper.update(null,wrapper);
		if(i>0){
			return true;
		}
		return false;
	}

	public Boolean isImageExist(String name){
		ApplicationHome applicationHome = new ApplicationHome(getClass());    //  系统获取文件位置
		String headerPath = applicationHome.getDir().getParentFile().getParentFile().getParentFile() + "\\resources\\files\\images\\";
		String fileName = getPrivateByName(name).getHeaderUrl();
		File file = new File(headerPath+fileName);
		if (!file.exists()) {
			UpdateWrapper<Private> wrapper = new UpdateWrapper<>();
			wrapper.eq("name",name).set("header_url","");
			Integer i = privateMapper.update(null,wrapper);
			if (i>0){
				randomAddPrivateExperience(name,10,5);
				return true;
			}
			return false;
		}
		return false;
	}

	//  修改个人信息头像 本地文件上传
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
		String oldFilePath = getPrivateByName(name).getHeaderUrl();    //  删除老的
		Boolean delete = false;

		//  修改 private 数据表
		UpdateWrapper<Private> wrapper = new UpdateWrapper<>();
		wrapper.eq("name",name).set("header_url",newFilePath);
		int i = privateMapper.update(null,wrapper);
		if(oldFilePath.isEmpty() || oldFilePath == null){
			System.out.println("文件为空");
		}else {
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

