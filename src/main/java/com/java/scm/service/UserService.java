package com.java.scm.service;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.User;
import com.java.scm.bean.so.UserSO;

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
    User login(String userName,String password);

    /**
     * 登出
     * @return
     */
    void logout();

    /**
     * 创建用户
     * @param user
     * @return
     */
    void addUser(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    void modifyUser(User user);

    /**
     * 停用账户
     * @param id
     * @return
     */
    void stopUsing(String id);

    /**
     * 删除账户
     * @param id
     * @return
     */
    void deleteUser(String id);

    /**
     * 密码重置
     * @param id
     * @return
     */
    void updatePassword(String id, String password);

    /**
     * 用户列表展示
     * @return
     */
    PageInfo<User> list(UserSO userSO);

    /**
     * 根据主键获取用户信息
     * @param id
     * @return
     */
    User getUserById(String id);
}
