package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @author hyh
 * @date 2024/4/2
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;
//    @Test
//    void testAddUser() {
//        User user = new User();
//        user.setUsername("cat");
//        user.setUserAccount("001");
//        user.setAvatarUrl("https://cn.bing.com/images/search?q=%E5%9B%BE%E7%89%87&FORM=IQFRBA&id=8EA47C03DC5EF4998C0487D376C492D2577760B1");
//        user.setGender(0);
//        user.setUserPassword("123456");
//        user.setPhone("123");
//        user.setEmail("123@qq.com");
//        boolean b = userService.save(user);
//        System.out.println(user.getId());
//        Assertions.assertTrue(b);
//    }

//    @Test
//    void userRegister() {
//        String userAccount = "yupi";
//        String userPassword = "12345678";
//        String checkPassword = "12345678";
////        long result = userService.userRegister(userAccount, userPassword, checkPassword);
////        Assertions.assertTrue(result > 0);
//    }
}