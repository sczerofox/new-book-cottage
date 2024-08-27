package com.hmlc.springboot;

import com.hmlc.springboot.service.*;
import com.hmlc.springboot.util.FolderUtils;
import com.hmlc.springboot.util.StringUtils;
import com.hmlc.springboot.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;


@SpringBootTest
class SpringbootApplicationTests {

    @Resource
    private AuthorService authorService;

    @Resource
    private PrivateService privateService;

    @Resource
    private UserService userService;

    @Resource
    private BookService bookService;

    @Resource
    private AuthService authService;

    @Autowired
    private Environment env;

    @Test
    void contextLoads() {

        String port = "9090";
        String str = "8e370569858f44338853d7a385ec6aa7.png,7c276576c0c54729afe1e00c7fadae88.png,f3f1a4302c35489895014fecd40c7c50.png,1bcdb56b0623406da491125a87fa2b94.png,821d6525dea94c4bb7f7cdbb5d5043c3.jpg";
        String[] strings = str.split(",");
        for(int i=0; i < strings.length; i++){
            Array.set(strings,i,"http://localhost:"+port+"/images/"+strings[i]);
        }




//        String port = env.getProperty("server.port");
//        System.out.println(env);

//        String port = System.getProperty("server.port");
//        System.out.println(port);
        /* ApplicationHome applicationHome = new ApplicationHome(getClass());    //  系统获取文件
        String per = applicationHome.getDir().getParentFile().getAbsolutePath()
                + "\\resources\\files\\images"; //   获取文件目录的绝对路径
        System.out.println(per);
        File uploadDir = new File(per);



        if (!uploadDir.exists() || !uploadDir.isDirectory()) {

        }*/
    }
//        ApplicationHome applicationHome = new ApplicationHome(getClass());    //  系统获取文件
//        String per = applicationHome.getDir().getParentFile().getAbsolutePath()
//          + "\\springboot\\src\\main\\resources\\static\\";   //   获取文件目录的绝对路径
//        System.out.println(per);
    //      + "\\springboot\\src\\main\\resources\\static\\";

//System.out.println("上传路径不存在");
//    File folder = new File(per);

    // 如果文件夹不存在，则创建
//            if (!folder.exists()) {
//        if (folder.mkdir()) {
//            System.out.println("文件夹创建成功: " + per);
//        } else {
//            System.out.println("文件夹创建失败");
//        }
//    } else {
//        System.out.println("文件夹已存在: " + per);
//    }
}
