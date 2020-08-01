package com.java.scm.dao;

import com.java.scm.bean.User;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Mapper
public interface UserDao extends TkMapper<User> {

    List<User> listUser(@Param("key") String key);


}
