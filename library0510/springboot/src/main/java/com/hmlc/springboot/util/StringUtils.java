package com.hmlc.springboot.util;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

/**
 * @ClassName StringUtils
 * @Description 字符串处理
 * @Author lazyFox
 * @Date 2024/6/6 15:49
 * @Version V0.1
 */
public class StringUtils {

	/**
	 * @Author LazyFox
	 * @Description 字符串非空处理器
	 * 检查一个或多个字符串是否为空或仅包含空白字符。
	 * @param strings 要检查的字符串列表
	 * @return 如果所有字符串都不为空且不只包含空白字符，则返回true；否则返回false。
	 * @Date 16:28 2024/6/6
	 */
	public static boolean isEmpty(String... strings) {
		for (String str : strings) {
			if (str == null || str.trim().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @Author LazyFox
	 * @Description 字符集拆解
	 * @Date 0:15 2024/6/14
	 */
	public static String[] toStringArray(String string){
		if(isEmpty(string)){
			string = "0";
			return string.split(",");
		}
		return string.split(",");
	}

	/**
	 * @Author LazyFox
	 * @Description 随机字符串生成工具 v1
	 * @Date 16:28 2024/6/6
	 */
	public static String getRandomStringV1(int length) {
		return UUID.randomUUID().toString().replace("-", "").substring(0, length);
	}


	/**
	 * @Author LazyFox
	 * @Description 随机字符生成 v2
	 * @Date 9:05 2024/6/8
	 */
	public static String getRandomStringV2(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("长度必须大于0");
		}

		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		characters += "0123456789"; // 添加少量数字

		StringBuilder sb = new StringBuilder(length);
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characters.length());
			sb.append(characters.charAt(index));
		}

		return sb.toString();
	}

	public static String encodeFilePathForUrl(String filePath) {
		try {
			String[] FilePath = filePath.split(":");
			// 将路径中的反斜杠替换为正斜杠
			String path = FilePath[1].replace("\\", "/");
			// 对路径进行URL编码
			String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
			return encodedPath;
		} catch (Exception e) {
			// 处理异常
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Author LazyFox
	 * @Description uuid 加密 （about）
	 * @Date 20:45 2024/6/26
	 */
	public static String uuidEncrypt(String str){
		String[] strings = str.split("-");
		Array.set(strings,strings.length-1,"****");
		return String.join("-",strings);
	}

	public static String[] filesImages(String iconImg, Integer port){
		String[] strings = iconImg.split(",");
		for(int i=0; i < strings.length; i++){
			Array.set(strings,i,"http://localhost:"+port+"/images/"+strings[i]);
		}
		return strings;
	}
}

