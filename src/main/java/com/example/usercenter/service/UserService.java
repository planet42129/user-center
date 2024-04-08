package com.example.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usercenter.model.domain.User;


import javax.servlet.http.HttpServletRequest;

/**
* @author yhh
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-04-03 14:08:08
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户信息脱敏
     * @param originalUser
     * @return
     */
    User getSafeUser(User originalUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
