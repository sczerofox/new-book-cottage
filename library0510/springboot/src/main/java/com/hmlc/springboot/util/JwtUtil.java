package com.hmlc.springboot.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

	private static long time = 1000 * 60 * 60 * 1; //
	private static String sign = "login";

	//生成一个token
	public static String createJwt() {
		// 创建一个JwtBuilder对象
		JwtBuilder jwtBuilder = Jwts.builder();

		String jwtToken = jwtBuilder
				// Header: 头部
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "HS256")
				// Payload: 载荷
				.setExpiration(new Date(System.currentTimeMillis() + time))
				// Signature: 签名
				.signWith(SignatureAlgorithm.HS256, sign)
				// 使用“.”连接成一个完整的字符串
				.compact();
		return jwtToken;
	}

	//生成一个token
	public static String createTokenCode(String type, Integer Time, String key) {
		// 创建一个JwtBuilder对象
		JwtBuilder jwtBuilder = Jwts.builder();

		long existTime;

		switch (type){
			case "h":
				existTime = 1000 * 60 * 60 * Time;
				break;
			case "m":
				existTime = 1000 * 60 * Time;
				break;
			case "s":
				existTime = 1000 * Time;
				break;
			default:
				existTime = 1000 * 60 * 60;
		}

		String jwtToken = jwtBuilder
				// Header: 头部
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "HS256")
				// Payload: 载荷
				.setExpiration(new Date(System.currentTimeMillis() + existTime))
				// Signature: 签名
				.signWith(SignatureAlgorithm.HS256, key)
				// 使用“.”连接成一个完整的字符串
				.compact();
		return jwtToken;
	}

	// 校验token
	public static boolean checkToken(String token) {
		if (token == null || token.equals("")) {
			System.out.println("token为空");
			return false;
		}

		try {
			Jwts.parser().setSigningKey(sign).parseClaimsJws(token);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// 校验token
	public static boolean checkTokenByKey(String key,String token) {
		if (token == null || token.equals("")) {
			System.out.println("token为空");
			return false;
		}

		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

