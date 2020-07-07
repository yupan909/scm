package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.*;
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
     * 工程名称
     */
    private String project;

    /**
     * 物资名称
     */
    private String product;

    /**
     * 物资型号
     */
    private String model;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 经手人
     */
    private String handle;

    /**
     * 物资来源
     */
    private String source;

    /**
     * 物资单价（元）
     */
    private BigDecimal price;

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
     * 创建人
     */
    private String createUser;
}