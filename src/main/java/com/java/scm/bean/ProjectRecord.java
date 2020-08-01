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
 * 工程流水账表
 *
 * @author yupan
 * @date 2020-06-23 22:08
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_record")
public class ProjectRecord {

    /**
     * 工程流水帐id
     */
    @Id
    @KeySql(order = ORDER.BEFORE, sql = "select uuid_short()")
    private String id;

    /**
     * 工程id
     */
    private String projectId;

    /**
     * 流水帐类型 0：收入 1：支出
     */
    private Byte type;

    /**
     * 日期
     */
    private String recordDate;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 备注
     */
    private String remark;

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

}
