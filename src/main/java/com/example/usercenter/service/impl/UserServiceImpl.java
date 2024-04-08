package com.example.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenter.common.ErrorCode;
import com.example.usercenter.common.ResultUtils;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.mapper.UserMapper;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @author yhh
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-04-03 14:08:08
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码加密
     */
    static final String SALT = "nj";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码小于8位");
        }
        //账户不能包含特殊字符
        String userAccountPattern = "^[a-zA-Z0-9_-]{4,16}$";
        Matcher matcher = Pattern.compile(userAccountPattern).matcher(userAccount);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号包含特殊字符");
        }
        //todo 密码正则
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户注册两次输入的密码不一致");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户账号已存在");
        }
        //2 加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //3 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册用户数据库插入失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 || userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号小于4为或密码小于8位");
        }
        //账户不能包含特殊字符
        String userAccountPattern = "^[a-zA-Z0-9_-]{4,16}$";
        Matcher matcher = Pattern.compile(userAccountPattern).matcher(userAccount);
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号包含特殊字符");
        }

        //2 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount can not match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        //3 用户信息脱敏
        User safeUser = getSafeUser(user);
        //4 记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safeUser);
        return safeUser;
    }


    /**
     * 用户信息脱敏
     *
     * @param originalUser
     * @return
     */
    @Override
    public User getSafeUser(User originalUser) {
        if (originalUser == null) {
            return null;
        }
        User safeUser = new User();
        safeUser.setId(originalUser.getId());
        safeUser.setUsername(originalUser.getUsername());
        safeUser.setUserAccount(originalUser.getUserAccount());
        safeUser.setAvatarUrl(originalUser.getAvatarUrl());
        safeUser.setGender(originalUser.getGender());
        safeUser.setPhone(originalUser.getPhone());
        safeUser.setUserStatus(0);
        safeUser.setUserRole(originalUser.getUserRole());
        safeUser.setEmail(originalUser.getEmail());
        safeUser.setCreateTime(originalUser.getCreateTime());
        return safeUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }
}




