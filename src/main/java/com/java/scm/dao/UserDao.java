package com.java.scm.dao;

import com.java.scm.bean.User;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Mapper
@Component
public interface UserDao extends TkMapper<User> {
}
