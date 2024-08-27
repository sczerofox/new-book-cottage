package com.hmlc.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hmlc.springboot.dto.common.Result;
import com.hmlc.springboot.entity.Private;
import com.hmlc.springboot.entity.User;
import com.hmlc.springboot.mapper.PrivateMapper;
import com.hmlc.springboot.mapper.UserMapper;
import com.hmlc.springboot.util.CaptchaV3Util;
import com.hmlc.springboot.util.JwtUtil;
import com.hmlc.springboot.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName AuthService
 * @Description 登录和注册的业务逻辑层
 * @Author lazyFox
 * @Date 2024/6/6 14:52
 * @Version V0.1
 */
@Service
public class AuthService {

	@Resource
	private UserMapper userMapper;
	@Resource
	private PrivateMapper privateMapper;
	@Resource
	private PrivateService privateService;

	public List<User> findAllUsers() {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.orderByDesc("card_num");
		return userMapper.selectList(wrapper);
	}

	public User getUsersByName(String name) {
		QueryWrapper<User> wrapper = new QueryWrapper();
		wrapper.eq("username",name);
		return userMapper.selectOne(wrapper);
	}

	public Integer getUsersMaxId() {
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.orderByDesc("user_id");
		List<User> users = userMapper.selectList(wrapper);
		return users.get(0).getUserId();
	}

	public Boolean updateUsersCardNumByName(String name,String cardNum) {
		UpdateWrapper<User> wrapper = new UpdateWrapper();
		wrapper.eq("username",name).set("card_num",cardNum);
		Integer i = userMapper.update(null,wrapper);
		if(i>0){
			return true;
		}
		return false;
	}

	/**
	 * @Author LazyFox
	 * @Description 借书证号
	 * @Date 19:50 2024/6/6
	 */
	public Result generateNewCardNum(String name) {
		Result result = new Result();

		// 检查用户名是否为空
		if (StringUtils.isEmpty(name)) {
			result.setCode(0);
			result.setMessage("用户名不能为空");
			return result;
		}

		// 检查用户是否存在
		User user = getUsersByName(name);
		if (user == null) {
			result.setCode(0);
			result.setMessage("用户不存在");
			return result;
		}

		// 检查用户是否已有借书证号
		String existingCardNum = user.getCardNum();
		if (!StringUtils.isEmpty(existingCardNum)) {
			result.setCode(0);
			result.setMessage("借书证号不能重复申请");
			return result;
		}

		// 获取所有用户的借书证号
		List<User> users = findAllUsers();
		String firstCardNum = users.get(0).getCardNum();

		// 提取VIP标识和日期部分
		String vipPrefix = firstCardNum.substring(0, 3);
		String dateStr = firstCardNum.substring(3);

		try {
			// 将日期部分转换为整数并加1
			int date = Integer.parseInt(dateStr);
			int newDate = date + 1;

			// 拼接成新的卡号
			result.setCode(1);
			//   修改数据库数据
			result.setData(updateUsersCardNumByName(name,vipPrefix + newDate));
			result.setMessage(vipPrefix + newDate);
			return result;
		} catch (NumberFormatException e) {
			// 异常处理：如果转换失败，返回原卡号或合适的错误标识
			return result;
		}
	}

	/**
	 * @Author LazyFox
	 * @Description 查询数据库中是否有该用户名
	 * @Date 15:26 2024/6/6
	 */
	public User queryUserByUsername(String username){
		QueryWrapper wrapper = new QueryWrapper<>();
		wrapper.eq("username",username);
		return userMapper.selectOne(wrapper);
	}


	/**
	 * @Author LazyFox
	 * @Description 用户手机号是否重复注册
	 * @Date 19:31 2024/6/6
	 */
	public boolean isUserPhoneExist(String phone) {
		// 使用lambda表达式简化查询条件
		User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, phone));
		// 直接返回查询结果的非判断，null表示不存在，非null表示存在
		return user != null;
	}


	/**
	 * @Author LazyFox
	 * @Description 查询用户的登录状态
	 * @Date 22:21 2024/6/8
	 */
	//  当用户状态不为 0 时返回true为(登录状态)  否则返回false为(未登录状态)  预防null指针错误
	public boolean isLoginState(String username) {
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq("username", username);
		User user = userMapper.selectOne(wrapper);
		return user != null && user.getIsLogin() != 0;
	}


	/**
	 * @Author LazyFox
	 * @Description 修改用户登录状态
	 * @Date 10:19 2024/6/8
	 */
	public boolean updateLoginState(String username, Integer state) {
		UpdateWrapper updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("username", username);
		updateWrapper.set("is_login", state);

		boolean isUpdateSuccess = userMapper.update(null, updateWrapper) > 0; // 检查更新操作是否成功
		return isUpdateSuccess;
	}


	/**
	 * @Author LazyFox
	 * @Description 验证鉴权码
	 * @Date 23:25 2024/6/6
	 */
	public Result checkToken(String key, String token) {
		// 检查key和token是否为空
		if (StringUtils.isEmpty(key,token)) {
			return createResult("key或token为空", -2, null);
		}

		try {
			// 验证token
			boolean isTokenValid = JwtUtil.checkTokenByKey(key, token);
			// 根据验证结果返回相应的Result对象
			return isTokenValid ? createResult("token验证通过", 0, null) : createResult("token无效", -1, null);
		} catch (Exception e) {
			// 异常处理：token验证过程中出现错误
			return createResult("token验证过程中出现错误！", -3, null);
		}
	}

	private Result createResult(String message, int code, Object data) {
		Result result = new Result();
		result.setMessage(message);
		result.setCode(code);
		result.setData(data);
		return result;
	}


	/**
	 * @Author LazyFox
	 * @Description 验证码验证
	 * @Date 21:32 2024/6/6
	 */
	public Result checkCaptcha(String phone, String captcha) {
		Result result = new Result();

		// 调用CaptchaV2Util的checkCaptcha方法来验证验证码
		// 这个方法会返回一个整数，表示验证结果
		Integer code = CaptchaV3Util.checkCaptcha(phone, captcha);
		result.setCode(code);

		// 根据验证结果设置相应的信息
		switch (code) {
			case 1: // 验证码正确
				result.setMessage("通过验证！");
				break;
			case -1: // 验证码错误
				result.setMessage("验证码错误！");
				break;
			case -2: // 验证码过期
				result.setMessage("验证码已过期！");
				break;
		}

		// 设置数据字段为null，因为没有需要返回的数据
		result.setData("null");
		return result;
	}

	public Integer registerPrivateTable(String name){
		Private pri = new Private(null,name,"",0,10,2000,"","",0,0);
		return privateMapper.insert(pri);
	}

	/**
	 * @Author LazyFox
	 * @Description 用户注册
	 * @Date 18:41 2024/6/6
	 */
	public Result register(String username, String password, String phone) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<>();

		// 检查用户名是否存在
		if (queryUserByUsername(username) != null) {
			result.setCode(-2);
			result.setMessage("用户名已存在");
			return result;
		}

		// 检查手机号是否已注册
		if (isUserPhoneExist(phone)) {
			result.setCode(-3);
			result.setMessage("手机号不能重复注册");
			return result;
		}

		// 创建并保存用户信息
		Date date = new Date();
		User user = new User(null, username, "", "", password, "", phone, "", 0, date,0);
		if (userMapper.insert(user) != 1) { // 检查插入是否成功
			result.setCode(-4);
			result.setMessage("注册失败，请重试");
			return result;
		}

		Integer i = registerPrivateTable(username);
		result.setCode(0);
		result.setMessage("注册成功");
		data.put("registerPri",i);
		result.setData(data);

		return result;
	}

	/**
	 * @Author LazyFox
	 * @Description 用户退出方法
	 * @Date 10:47 2024/6/8
	 */
	public Result outLogin(String username, String key) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<>();

		// 检查用户名和key是否为空
		if (StringUtils.isEmpty(username,key)) {
			result.setCode(-1);
			result.setMessage("用户名不能为空");
			result.setData("null");
			return result;
		}

		// 更新登录状态
		Integer state = 0; // 退出登录状态
		Boolean is_login = updateLoginState(username, state);
		Boolean addExp = false;
		if(privateService.isPrivateExistByName(username)){
			addExp = privateService.randomAddPrivateExperience(username,2,1);
		}
		if (is_login) {
			data.put("loginState", state);
			data.put("username", username);
			data.put("addExp", addExp);

			result.setCode(0);
			result.setMessage("退出成功");
			result.setData(data);
			return result;
		} else {
			result.setCode(-2);
			result.setMessage("退出失败");
			result.setData(data);
			return result;
		}
	}



	/**
	 * @Author LazyFox
	 * @Description 用户登录逻辑判断 和 数据生成
	 * @Date 16:25 2024/6/6
	 */
	public Result login(String username, String password) {
		Result result = new Result();
		Map<String, Object> data = new HashMap<>();

		// 检查用户名是否为空
		if (StringUtils.isEmpty(username)) {
			result.setCode(-1);
			result.setMessage("用户名不能为空");
			result.setData("null");
			return result;
		}

		// 检查密码是否为空
		if (StringUtils.isEmpty(password)) {
			result.setCode(-1);
			result.setMessage("密码不能为空");
			result.setData("null");
			return result;
		}

		// 检查用户名是否存在
		User user = queryUserByUsername(username);
		if (user == null) {
			result.setCode(-2);
			result.setMessage("用户名不存在");
			result.setData("null");
			return result;
		}

		// 检查密码是否正确
		if (!user.getPassword().equals(password)) {
			result.setCode(-3);
			result.setMessage("密码错误");
			result.setData("null");
			return result;
		}

		if(isLoginState(username)){
			result.setCode(-4);
			result.setMessage("账号已在别处登录");
			result.setData("null");
			return result;
		}

		// 登录成功，生成并返回token
		Integer state = 1;    //  登录默认为 1  退出登录默认为 0
		String key = StringUtils.getRandomStringV2(6);
		String token = JwtUtil.createTokenCode("m", 30, key);
		Boolean is_login = updateLoginState(username,state);
		Boolean addExp = false;
		if(privateService.isPrivateExistByName(username)){
			addExp = privateService.randomAddPrivateExperience(username,10,5);
		}

		data.put("key", key);
		data.put("token", token);
		data.put("username", username);
		data.put("logState", state);
		data.put("isLog", is_login);
		data.put("addExp", addExp);

		result.setCode(0);
		result.setMessage("登录成功");
		result.setData(data);
		return result;
	}

}

