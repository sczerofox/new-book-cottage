package com.hmlc.springboot.service;

import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.About;
import com.hmlc.springboot.mapper.AboutMapper;
import com.hmlc.springboot.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AboutService
 * @Description 关于的业务逻辑处理层
 * @Author lazyFox
 * @Date 2024/6/26 20:14
 * @Version V0.1
 */
@Service
public class AboutService {

	@Resource
	private AboutMapper aboutMapper;
	@Autowired
	private Environment env;

	public Result findAboutById(Integer id){
		Result result = new Result();
		Map<String,Object> map = new HashMap<>();

		About about = aboutMapper.selectById(id);
		if(about == null){
			result.setCode(0);
			result.setMessage("版权不存在");
			return result;
		}

		about.setUuid(StringUtils.uuidEncrypt(about.getUuid()));
		int port = env.getProperty("server.port", Integer.class);
		String[] images = StringUtils.filesImages(about.getIconImg(),port);
		about.setIconImg("...");

		map.put("about",about);
		map.put("images",images);
		result.setCode(1);
		result.setMessage("ok");
		result.setData(map);
		return result;
	}
}

