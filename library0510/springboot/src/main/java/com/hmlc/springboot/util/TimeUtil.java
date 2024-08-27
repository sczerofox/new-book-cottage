package com.hmlc.springboot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * @Author LazyFox
	 * @Description 获取当前时间 v2
	 * @Date 9:11 2024/6/8
	 */
	public static String getCurrentTime() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DATE_TIME_FORMATTER);
	}


	/**
	 * @Author LazyFox
	 * @Description 设置过期时间 v2
	 * @Date 9:13 2024/6/8
	 */
	public static String calculateExpirationTime(String time, int minute) {
		try {
			LocalDateTime dateTime = LocalDateTime.parse(time, DATE_TIME_FORMATTER);
			LocalDateTime expirationTime = dateTime.plusMinutes(minute);
			return expirationTime.format(DATE_TIME_FORMATTER);
		} catch (Exception e) {
			// 更优雅地处理异常，例如返回null或默认时间
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * @Author LazyFox
	 * @Description Long 型 时间格式转 String 型
	 * @Date 9:17 2024/6/8
	 */
	public static String timeMillisToString(Long timeMillis) {
		Instant instant = Instant.ofEpochMilli(timeMillis);
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return dateTime.format(DATE_TIME_FORMATTER);
	}


	/**
	 * @Author LazyFox
	 * @Description 时间延迟方法
	 * @Date 9:26 2024/6/8
	 */
	public static void timeDelayFunction(int second) {    //  second 秒钟  minutes 分钟
		try {
			// 将分钟转换为毫秒（1分钟 = 60,000毫秒）
//			int milliseconds = minutes * 60 * 1000;
			int milliseconds = second * 1000;
			System.out.println("延时器正在运行~");
			// 使当前线程暂停指定的毫秒数
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// 如果线程在睡眠时被中断，这里会捕获到InterruptedException
			// 这里简单地打印了异常信息，实际应用中可能需要更复杂的处理
			System.out.println("线程睡眠被中断: " + e.getMessage());
		}
	}


}
