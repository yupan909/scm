package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
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
     * 客户名称
     */
    private String customer;

    /**
     * 工程名称
     */
    private String name;

    /**
     * 工程内容
     */
    private String content;

    /**
     * 工程进度
     */
    private String progress;

    /**
     * 合同金额
     */
    private BigDecimal contractMoney;

    /**
     * 结算金额
     */
    private BigDecimal finalMoney;

    /**
     * 状态 0：启用 1：禁用
     */
    private Byte state;

    @Transient
    private String stateInfo;

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

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 修改人id
     */
    private String updateUserId;

    /**
     * 收入金额
     */
    @Transient
    private BigDecimal inMoney;

    /**
     * 支出金额
     */
    @Transient
    private BigDecimal outMoney;

    /**
     * 合计金额
     */
    @Transient
    private BigDecimal sumMoney;

}
