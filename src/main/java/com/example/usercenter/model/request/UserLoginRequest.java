package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hyh
 * @date 2024/4/2
 */
@Data
public class UserLoginRequest implements Serializable {

    private String userAccount;
    private String userPassword;
}