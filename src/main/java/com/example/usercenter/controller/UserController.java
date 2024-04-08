package com.example.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ErrorCode;
import com.example.usercenter.common.ResultUtils;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.model.request.UserLoginRequest;
import com.example.usercenter.model.request.UserRegisterRequest;
import com.example.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.example.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * 用户接口
 *
 * @author hyh
 * @date 2024/4/2
 */
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long l = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(l);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);

    }
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout( HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        int res = userService.userLogout(request);
        return ResultUtils.success(res);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObject;
        //todo 校验用户是否合法
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
//        return userService.getSafeUser(user);
        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {

        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> users = userList.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
        return ResultUtils.success(users);
//        return users;

    }

    //todo 不是管理员删除用户时 返回响应信息不对 返回了50000
    @RequestMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员权限");
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不存在");
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 是否为管理员
     *
     * @param request 获取session
     * @return 布尔值
     */
    private boolean isAdmin(HttpServletRequest request) {
        //鉴权 仅管理员
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObject;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }
}
