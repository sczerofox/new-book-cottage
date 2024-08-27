package com.hmlc.springboot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @ClassName FilesUploadUtil
 * @Description 文件上传工具类
 * @Author lazyFox
 * @Date 2024/6/8 0:22
 * @Version V0.1
 */
public class FilesUtils {

	/**
	 * @Author LazyFox
	 * @Description 文件上传方法工具
	 * @Date 1:10 2024/6/8
	 */
	public static String filesUpload(MultipartFile file, Integer type, Integer port) {
		//  files 校验

		if (file == null||file.isEmpty()) {
			return "上传失败，文件空。";
		}

		//  文件重命名 避免数据错乱
		String originalFilename = file.getOriginalFilename();  //  原来的图片名
		String ext = "."+originalFilename.split("\\.")[1];  //  拆分原来文件的后缀名 1.png  1 是数组下标[0]  png 是下标[1]
		String uuid = UUID.randomUUID().toString().replace("-","");  //  随机生成 uuid 以防止图片重复覆盖

		String fileName = uuid + ext;  //  拼接成为新的 图片名

		//  上传图片
		ApplicationHome applicationHome = new ApplicationHome(FilesUtils.class);    //  系统获取文件
		String per1 = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath();  //  获取主文件目录的绝对路径
		String per2	= "\\resources\\files\\images\\";
		String per = per1 + per2;
		String path = per + fileName;    //  文件路径

		// 检查上传路径是否存在
		File uploadDir = new File(per);
		if (!uploadDir.exists() || !uploadDir.isDirectory()) {
			FolderUtils.createFolders(per1,per2);
		}

		// 上传图片
		try {
			file.transferTo(new File(path));
		} catch (IOException e) {
			return "上传失败，请重试。";
		}

		// 返回文件路径
		switch (type) {
			case 1:
				return path;
			case 3:
				return "http://localhost:"+port+"/images/"+fileName;
			case 2:
			default:
				return fileName;
		}
	}

	/**
	 * @Author LazyFox
	 * @Description 文件根据文件系统路径删除方法
	 * @Date 1:10 2024/6/8
	 */
	public static boolean deleteFileByPath(String filePath) {

		if (filePath == null || filePath.isEmpty()) {
			return false;
		}

		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}

		try {
			return file.delete();
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @Author LazyFox
	 * @Description 文件根据文件系统路径和文件名删除方法
	 * @Date 1:10 2024/6/8
	 */
	public static boolean deleteFileByFilename(String filename) {
		// 获取文件路径
		ApplicationHome applicationHome = new ApplicationHome(FilesUtils.class);    //  系统获取文件
		String per1 = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath();  //  获取主文件目录的绝对路径
		String per2	= "\\resources\\files\\images\\";
		String per = per1 + per2;
		String path = per + filename;

		// 检查文件是否存在
		File file = new File(path);
		if (file.exists()) {
			// 删除文件
			if (file.delete()) {
				// 文件删除成功
//				System.out.println("文件删除成功");
				return true;
			} else {
				// 文件删除失败
				System.out.println("文件删除失败");
				return false;
			}
		} else {
			// 文件不存在
			System.out.println("文件不存在");
			return false;
		}
	}

	public static ResponseEntity<Resource> getImageByAllFilepath(String filepath) {
		try {
			Resource resource = new UrlResource("file:///"+filepath);
			if (resource != null) {
				return ResponseEntity.ok()
						.contentType(MediaType.IMAGE_JPEG)
						.body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	public static ResponseEntity<Resource> getImageByFilename(String filename) {
		try {
			ApplicationHome applicationHome = new ApplicationHome(FilesUtils.class);    //  系统获取文件
			String per1 = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath();  //  获取主文件目录的绝对路径
			String per2	= "\\resources\\files\\images\\";
			String per = per1 + per2;

			Path path = Paths.get(per + filename);
			Resource resource = new UrlResource(path.toUri());
			File file = new File(path.toString());
			if(file.exists()){
				if (resource != null) {
					return ResponseEntity.ok()
							.contentType(MediaType.IMAGE_JPEG)
							.body(resource);
				}
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

}

