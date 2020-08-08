package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.so.UserSO;
import com.java.scm.config.aop.anno.AdminRight;
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
        User user = userService.login(userName,password);
        return new BaseResult(user);
    }

    /**
     * 用户退出登录
     * @return
     */
    @GetMapping("/logout")
    public BaseResult logout(){
        userService.logout();
        return BaseResult.successResult();
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    @AdminRight
    public BaseResult addUser(@RequestBody User user){
        userService.addUser(user);
        return BaseResult.successResult();
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PostMapping("/modify")
    @AdminRight
    public BaseResult modifyUser(@RequestBody User user){
        userService.modifyUser(user);
        return BaseResult.successResult();
    }

    /**
     * 停用账号
     * @return
     */
    @GetMapping("/stopUsing/{id}")
    @AdminRight
    public BaseResult stopUsing(@PathVariable("id") String id){
        userService.stopUsing(id);
        return BaseResult.successResult();
    }

    /**
     * 删除账号
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @AdminRight
    public BaseResult deleteUser(@PathVariable("id") String id){
        userService.deleteUser(id);
        return BaseResult.successResult();
    }

    /**
     * 修改密码
     * @return
     */
    @PostMapping("/updatePassword")
    public BaseResult updatePassword(@RequestBody User user){
        userService.updatePassword(user.getId(), user.getPassword());
        return BaseResult.successResult();
    }

    /**
     * 密码重置
     * @return
     */
    @GetMapping("/resetPassword/{id}")
    @AdminRight
    public BaseResult resetPassword(@PathVariable("id") String id){
         userService.updatePassword(id, CommonConsts.RESET_PASSWORD);
        return BaseResult.successResult();
    }

    /**
     * 查询人员列表
     * @return
     */
    @PostMapping("/list")
    @AdminRight
    public BaseResult list(@RequestBody UserSO userSO){
        PageInfo<User> pageInfo = userService.list(userSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 根据主键查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public BaseResult getOne(@PathVariable("id") String id){
        User user = userService.getUserById(id);
        return new BaseResult(user);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/authority")
    public BaseResult authority(){
        User user = RequestUtil.getCurrentUser();
        return new BaseResult(user);
    }
}
