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
 * 出入库表
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inout_stock")
public class InoutStock {

    /**
     * 出入库Id
     */
    @Id
    @KeySql(order = ORDER.BEFORE, sql = "select uuid_short()")
    private String id;

    /**
     * 仓库id
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    @Transient
    private String warehouseName;

    /**
     * 工程id
     */
    private String projectId;

    /**
     * 工程名称
     */
    @Transient
    private String project;

    /**
     * 库存id
     */
    private String stockId;

    /**
     * 物资名称
     */
    @Transient
    private String product;

    /**
     * 物资型号
     */
    @Transient
    private String model;

    /**
     * 单位
     */
    @Transient
    private String unit;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 物资单价（元）
     */
    private BigDecimal price;

    /**
     * 物资来源
     */
    private String source;

    /**
     * 经手人
     */
    private String handle;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类别 0：入库 1：出库
     */
    private Byte type;

    /**
     * 类别名称
     */
    @Transient
    private String typeText;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 创建人
     */
    @Transient
    private String createUser;

    /**
     * 入库数量（工程物资统计）
     */
    @Transient
    private Integer inCount;

    /**
     * 出库数量（工程物资统计）
     */
    @Transient
    private Integer outCount;


}