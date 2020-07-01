package com.java.scm.service;

import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;

/**
 * 用户相关服务
 * @author hujunhui
 * @date 2020/6/24
 */
public interface UserService {

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    BaseResult login(String userName,String password);

    /**
     * 登出
     * @return
     */
    BaseResult logout();

    /**
     * 创建用户
     * @param user
     * @return
     */
    BaseResult addUser(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    BaseResult modifyUser(User user);

    /**
     * 停用账户
     * @param id
     * @return
     */
    BaseResult stopUsing(Integer id);

    /**
     * 删除账户
     * @param id
     * @return
     */
    BaseResult deleteUser(Integer id);

    /**
     * 密码重置
     * @param id
     * @return
     */
    BaseResult updatePassword(Integer id, String password);

    /**
     * 用户列表展示
     * @param key
     * @param pageNum
     * @param pageSize
     * @return
     */
    BaseResult list(String key,int pageNum,int pageSize);

    /**
     * 根据主键获取用户信息
     * @param id
     * @return
     */
    BaseResult getUserById(Long id);
}
