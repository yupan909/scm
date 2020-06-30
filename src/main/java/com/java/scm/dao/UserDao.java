package com.java.scm.dao;

import com.java.scm.bean.User;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Mapper
@Component
public interface UserDao extends TkMapper<User> {

    /**
     * 提取用户列表
     * @param key
     * @return
     */

    @Results({
            @Result(column="warehouse_id", property="warehouseId")
    })
    @Select("SELECT u.`id`,u.`warehouse_id`,u.`name`,u.`mobile`,u.`password`,u.`state`*1 AS state ,u.`admin`*1 AS admin ,u.`create_time`,u.`update_time` , w.name as warehouseInfo FROM user u LEFT JOIN warehouse w ON u.`warehouse_id` = w.`id` WHERE u.mobile != 'admin' AND (u.name LIKE CONCAT('%',#{key},'%') OR u.mobile LIKE CONCAT('%',#{key},'%') ) order by u.create_time desc")
    List<Map> getUserInfos(@Param("key") String key);

}
