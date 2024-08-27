package com.hmlc.springboot.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName CheckCodeUtil
 * @Description 随机验证码生成
 * @Author lazyFox
 * @Date 2024/6/4 23:07
 * @Version V0.3
 */
public class CaptchaV3Util {

	private static final ConcurrentHashMap<String, CaptchaInfo> CACHE = new ConcurrentHashMap<>();    //  公共数据缓池
	private static final long DEFAULT_EXPIRATION_TIME = 1000L * 60 * 5;   // 默认过期时间 5分钟

	//  自定义数据缓存类
	static class CaptchaInfo {
		String captcha;
		long expirationTime;

		CaptchaInfo(String captcha, long expirationTime) {
			this.captcha = captcha;
			this.expirationTime = expirationTime;
		}
	}

	//  生成随机6位数验证码
	private static String getRandomCaptcha() {
		return String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
	}

	//  生成过期时间 （自定义）
	private static long getExpirationTime(long time) {
		return System.currentTimeMillis() + (time * 60 * 1000L);
	}

	//  生成一个自定义过期时间的验证码
	public static String createCaptchaByTime(String name, Long time) {
		String captcha = getRandomCaptcha();
		long expirationTime = getExpirationTime(time != null ? time : DEFAULT_EXPIRATION_TIME);
		CACHE.put(name, new CaptchaInfo(captcha, expirationTime));

		return captcha;
	}

	//  验证码校验
	public static Integer checkCaptcha(String name, String captcha) {
		CaptchaInfo info = CACHE.get(name);

		if (info == null) {
			return -3;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime > info.expirationTime) {
			CACHE.remove(name);
			return -2;
		}

		if (info.captcha.equals(captcha)) {
			CACHE.remove(name);
			return 1;
		} else {
			CACHE.remove(name);
			return -1;
		}
	}
}


