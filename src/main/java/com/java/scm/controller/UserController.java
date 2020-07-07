package com.java.scm.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.scm.aop.anno.AdminRight;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.enums.CommonConsts;
import com.java.scm.service.UserService;
import com.java.scm.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户相关 控制器
 * @author hujunhui
 * @date 2020/6/24
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @return
     */
    @PostMapping("/login")
    public BaseResult login(){
        HttpServletRequest request = RequestUtil.getRequest();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        return userService.login(userName,password);
    }

    /**
     * 用户退出登录
     * @return
     */
    @GetMapping("/logout")
    public BaseResult logout(){
        return userService.logout();

    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    @AdminRight
    public BaseResult addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PostMapping("/modify")
    @AdminRight
    public BaseResult modifyUser(@RequestBody User user){
        return userService.modifyUser(user);
    }

    /**
     * 停用账号
     * @return
     */
    @GetMapping("/stopUsing/{id}")
    @AdminRight
    public BaseResult stopUsing(@PathVariable("id") String id){
        return userService.stopUsing(id);
    }

    /**
     * 删除账号
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @AdminRight
    public BaseResult deleteUser(@PathVariable("id") String id){
        return userService.deleteUser(id);
    }


    /**
     * 修改密码
     * @param params
     * @return
     */
    @PostMapping("/updatePassword")
    public BaseResult updatePassword(@RequestBody JSONObject params){
        String id = params.getString("id");
        String password = params.getString("password");
        return userService.updatePassword(id,password);
    }

    /**
     * 密码重置
     * @return
     */
    @GetMapping("/resetPassword/{id}")
    @AdminRight
    public BaseResult resetPassword(@PathVariable("id") String id){
        return userService.updatePassword(id, CommonConsts.RESET_PASSWORD);
    }

    /**
     * 查询人员列表
     * @param key  关键字，根据姓名，手机号
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    @AdminRight
    public BaseResult list(String key,int pageNum,int pageSize){
        return userService.list(key,pageNum,pageSize);
    }

    /**
     * 根据主键查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public BaseResult getOne(@PathVariable("id") String id){
        return userService.getUserById(id);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/authority")
    public BaseResult authority(){
        User user = RequestUtil.getCurrentUser();
        return  new BaseResult(user);
    }
}
