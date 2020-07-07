package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.*;
import java.util.Date;

/**
 * 工程表
 *
 * @author yupan
 * @date 2020-06-23 22:08
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project {

    /**
     * 工程ID
     */
    @Id
    @KeySql(order = ORDER.BEFORE, sql = "select uuid_short()")
    private String id;

    /**
     * 工程名称
     */
    private String name;

    /**
     * 设计内容
     */
    private String content;

    /**
     * 状态 0：启用 1：禁用
     */
    private Byte state;

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

    @Transient
    private String stateInfo;
}
