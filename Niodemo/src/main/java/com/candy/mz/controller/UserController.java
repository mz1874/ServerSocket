package com.candy.mz.controller;

import com.candy.mz.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author by wangchong
 * @Description
 * @Date 2020/7/17 12:09
 */
@RequestMapping(value = "user")
@RestController
public class UserController {

    @GetMapping(value = "login")
    public UserEntity login(HttpSession session) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserName("姣");
        userEntity.setPassWord("1234");
        session.setAttribute("user",userEntity.getUserName());
        return userEntity;
    }
}
