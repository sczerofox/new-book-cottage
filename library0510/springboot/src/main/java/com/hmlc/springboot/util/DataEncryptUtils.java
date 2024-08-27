package com.hmlc.springboot.util;

import com.hmlc.springboot.entity.User;

import java.util.List;

public class DataEncryptUtils {

    public static List<User> encryptResultByPassword(List<User> users,Integer max){
        for(int i=0;i < max;i++){
            users.get(i).setPassword("******");
        }
        return users;
    }
}
