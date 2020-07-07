package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements Serializable {

    /**
     * 用户id
     */
    @Id
    @KeySql(order = ORDER.BEFORE, sql = "select uuid_short()")
    private String id;

    /**
     * 仓库ID
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    @Transient
    private String warehouseName;

    /**
     * 用户名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态 0：启用 1：禁用
     */
    private Byte state;

    /**
     * 状态 0：非管理员 1：管理员
     */
    private Byte admin;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}