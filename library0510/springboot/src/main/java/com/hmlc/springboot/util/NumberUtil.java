package com.hmlc.springboot.util;

import java.util.Random;

/**
 * @ClassName NumberUtil
 * @Description 数字工具集
 * @Author lazyFox
 * @Date 2024/6/10 11:10
 * @Version V0.1
 */
public class NumberUtil {

	public static Integer randomNumberByInteger(Integer value){
		Random random = new Random();
		return random.nextInt(value)+1;
	}


	/**
	 * 生成一个在 max 和 min 之间的随机整数（包括 min 和 max）。
	 *
	 * @param max 上限
	 * @param min 下限
	 * @return 随机整数
	 */
	public static Integer randomNumberByMaxAndMin(Integer max, Integer min) {
		if (max < min) {
			throw new IllegalArgumentException("max must be greater than or equal to min");
		}
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}
}

