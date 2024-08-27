package com.hmlc.springboot.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {

    private String name;
    private String oldPassword;
    private String newPassword;
    private String rePassword;

}
