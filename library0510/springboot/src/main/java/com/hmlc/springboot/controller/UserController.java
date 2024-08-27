package com.hmlc.springboot.controller;

import com.hmlc.springboot.dto.common.PageBean;
import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.dto.user.ChangePassword;
import com.hmlc.springboot.entity.User;
import com.hmlc.springboot.service.UserService;
import com.hmlc.springboot.util.DataEncryptUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName UserCOntroller
 * @Description 用户接口网业跳转与数据传递
 * @Author lenovo
 * @Date 2024/5/25 22:54
 * @Version V1.0
 */


@RestController
@RequestMapping("/users")
public class UserController {


	@Resource
	private UserService userService;

	// 构建首页接口，查询全部数据
	@GetMapping("/home")
	public List<User> getFillAllData() {

		return DataEncryptUtils.encryptResultByPassword(userService.findAllUsers(),userService.getAllUsersMax());
	}

	// 构建分页查询接口，查询分页数据
	@GetMapping("/pagination")
	public PageBean getFillAllDataByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
		PageBean pb = userService.findAllUsersByPage(page, pageSize);
		pb.setList(DataEncryptUtils.encryptResultByPassword(pb.getList(),pageSize));
		return pb;
	}

	// 构建模糊查询接口，查询 username 的分页数据
	@GetMapping("/search")
	public PageBean getFillAllDataByObscureInfo(@RequestParam Integer page, @RequestParam Integer pageSize,
												@RequestParam String username) {
		return userService.findAllUsersByUserName(page, pageSize, username);
	}

	// 构建模糊查询接口，查询 username 的分页数据
	@GetMapping("/recoverData")
	public PageBean getFillAllDataByDeleteInfo(@RequestParam Integer page, @RequestParam Integer pageSize) {
		return userService.findAllDeleteUsersById(page, pageSize);
	}

	// 构建删除接口，删除图书信息
	@PostMapping("/remove")
	public Integer deleteUser(@RequestBody User user) {
		return userService.deleteUser(user);
	}

	// 构建恢复接口，恢复用户信息
	@PostMapping("/recover")
	public Integer recoverUser(@RequestBody User user) {
		return userService.recoverUser(user);
	}

	// 构建编辑接口，编辑图书信息
	@PostMapping("/edit")
	public Integer updateUser(@RequestBody User user) {
		user.setPassword(null);
		return userService.updateUser(user);
	}

	// 构建编辑接口，编辑图书信息
	@PostMapping("/updatePassword")
	public Result updateUserPassword(@RequestBody ChangePassword changePassword) {
		return userService.updateNewPassword(changePassword);
	}

	// 构建新增接口，新增图书信息
	@PostMapping("/add")
	public Integer insertUser(@RequestBody User user) {
		return userService.insertUser(user);
	}
}

