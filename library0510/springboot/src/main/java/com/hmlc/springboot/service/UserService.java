package com.hmlc.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.hmlc.springboot.dto.common.PageBean;
import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.dto.user.ChangePassword;
import com.hmlc.springboot.entity.User;
import com.hmlc.springboot.mapper.UserMapper;
import com.hmlc.springboot.util.ApiUtil;
import com.hmlc.springboot.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserService
 * @Description 用户业务与方法
 * @Author lenovo
 * @Date 2024/5/25 22:56
 * @Version V1.0
 */

@Service
public class UserService {

	@Resource
	private UserMapper userMapper;
	ApiUtil apiUtil = new ApiUtil();

	// 查询全部数据
	public List<User> findAllUsers() {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.orderByDesc("update_time");
		wrapper.eq("is_del", 0);
		return userMapper.selectList(wrapper);
//		return DataEncryptUtils.encryptResultByPassword(userMapper.selectList(wrapper),getAllUsersMax());
	}

	// 查询全部数据
	public User queryUserByUsername(String username) {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("username", username);
		wrapper.eq("is_del",0);
		return userMapper.selectOne(wrapper);
	}

	// 查询全部数据
	public Integer getAllUsersMax() {
		QueryWrapper<User> wrapper = new QueryWrapper();
		wrapper.orderByDesc("user_id");
		return userMapper.selectList(wrapper).get(0).getUserId();
	}

	// 分页查询全部数据
	public PageBean findAllUsersByPage(Integer page, Integer pageSize) {
		// 首先设置好分页参数
		PageHelper.startPage(page, pageSize);

		// 根据分页条件，从拿到的全部数据中去分页
		return apiUtil.usePageBeanByPages(page,pageSize,findAllUsers());
	}

	// 按照bookname进行模糊查询并分页
	public PageBean findAllUsersByUserName(Integer page, Integer pageSize, String username) {
		// 首先设置好分页参数
		PageHelper.startPage(page, pageSize);

		// 把模糊搜索条件构建好
		QueryWrapper queryWrapper = new QueryWrapper<>();
		queryWrapper.like("real_name", username);
		queryWrapper.orderByDesc("update_time");
		queryWrapper.eq("is_del",0);

		// 根据分页条件，从拿到的全部数据中去分页
		return apiUtil.usePageBeanByPages(page,pageSize,userMapper.selectList(queryWrapper));
	}

	// 按照 id 进行数据恢复查询并分页
	public PageBean findAllDeleteUsersById(Integer page, Integer pageSize) {
		// 首先设置好分页参数
		PageHelper.startPage(page, pageSize);

		// 把模糊搜索条件构建好
		QueryWrapper queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("update_time");
		queryWrapper.eq("is_del",1);

		// 根据分页条件，从拿到的全部数据中去分页
		return apiUtil.usePageBeanByPages(page,pageSize,userMapper.selectList(queryWrapper));
	}

	// 删除图书信息
	public Integer deleteUser(User user) {
		user.setIsDel(1);
		return userMapper.updateById(user);
	}

	// 恢复图书信息
	public Integer recoverUser(User user) {
		System.out.println(user.getUserId());
		user.setIsDel(0);
		return userMapper.updateById(user);
	}

	// 编辑图书信息
	public Integer updateUser(User user) {
		return userMapper.updateById(user);
	}

	//  修改用户名密码
	public Result updateNewPassword(ChangePassword changePassword){
		Result result = new Result();
		//  用户名校验
		if(StringUtils.isEmpty(changePassword.getName())){
			result.setCode(0);
			result.setMessage("用户名为空");
			return result;
		}

		//  密码为空校验
		if(StringUtils.isEmpty(changePassword.getOldPassword())){
			result.setCode(0);
			result.setMessage("原密码为空");
			return result;
		}
		if(StringUtils.isEmpty(changePassword.getNewPassword())){
			result.setCode(0);
			result.setMessage("新密码为空");
			return result;
		}
		if(StringUtils.isEmpty(changePassword.getRePassword())){
			result.setCode(0);
			result.setMessage("确认密码为空");
			return result;
		}

		//  密码一致校验
		if(!changePassword.getNewPassword().equals(changePassword.getRePassword())){
			result.setCode(0);
			result.setMessage("密码不一致");
			return result;
		}

		//  原密码校验
		if(!queryUserByUsername(changePassword.getName()).getPassword().equals(changePassword.getOldPassword())){
			result.setCode(0);
			result.setMessage("原密码错误");
			return result;
		}

		//  修改密码 操作
		if(queryUserByUsername(changePassword.getName()).getPassword().equals(changePassword.getOldPassword())){
			UpdateWrapper<User> wrapper = new UpdateWrapper();
			wrapper.eq("username",changePassword.getName()).set("password",changePassword.getNewPassword());
			Integer i = userMapper.update(null,wrapper);

			result.setCode(1);
			result.setMessage("修改成功");
			return result;
		}else {
			result.setCode(0);
			result.setMessage("修改错误");
			return result;
		}
	}

	// 新增图书信息
	public Integer insertUser(User user) {
		return userMapper.insert(user);
	}
}

