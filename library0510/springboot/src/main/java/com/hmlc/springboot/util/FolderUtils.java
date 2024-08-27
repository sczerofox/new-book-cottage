package com.hmlc.springboot.util;

import java.io.File;

/**
 * @ClassName FolderUtils
 * @Description 文件夹 管理类
 * @Author lazyFox
 * @Date 2024/6/23 16:00
 * @Version V0.1
 */
public class FolderUtils {

	public static void createFolders(String parentPath, String directoryPath) {
		// 创建File对象，表示要创建的目录
		File directory = new File(parentPath, directoryPath);

		// 如果目录不存在，则创建
		if (!directory.exists()) {
			if (directory.mkdirs()) {
//				System.out.println("目录创建成功: " + directory.getAbsolutePath());
			} else {
				System.out.println("目录创建失败");
			}
		} else {
			System.out.println("目录已存在: " + directory.getAbsolutePath());
		}
	}
}

