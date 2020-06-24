package com.java.scm.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
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
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        return userService.login(mobile,password);
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
    public BaseResult addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PostMapping("/modify")
    public BaseResult modifyUser(@RequestBody User user){
        return userService.modifyUser(user);
    }

    /**
     * 停用账号
     * @return
     */
    @GetMapping("/stopUsing/{id}")
    public BaseResult stopUsing(@PathVariable("id") Integer id){
        return userService.stopUsing(id);
    }

    /**
     * 删除账号
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResult deleteUser(@PathVariable("id") Integer id){
        return userService.deleteUser(id);
    }

    /**
     * 密码重置
     * @param params
     * @return
     */
    @PostMapping("/resetPassword")
    public BaseResult resetPassword(@RequestBody JSONObject params){
        Integer id = params.getInteger("id");
        String password = params.getString("password");
        return userService.resetPassword(id,password);
    }

    /**
     * 查询人员列表
     * @param name
     * @param mobile
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public BaseResult list(String name,String mobile,int pageNum,int pageSize){
        return userService.list(name,mobile,pageNum,pageSize);
    }
}
