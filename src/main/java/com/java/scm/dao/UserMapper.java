package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.User;
import com.java.scm.bean.so.UserSO;
import com.java.scm.tk.TkMapper;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
public interface UserMapper extends TkMapper<User> {

    /**
     * 用户列表
     * @return
     */
    Page<User> listUser(UserSO userSO);


}
