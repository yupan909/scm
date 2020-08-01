package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 库存记录表
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock_record")
public class StockRecord {

    /**
     * 库存变更记录id
     */
    @Id
    @KeySql(order = ORDER.BEFORE, sql = "select uuid_short()")
    private String id;

    /**
     * 库存id
     */
    private String stockId;

    /**
     * 出入库id
     */
    private String inoutStockId;

    /**
     * 工程名称
     */
    @Transient
    private String project;

    /**
     * 变更数量
     */
    private Integer count;

    /**
     * 变更类型 0：入库 1：出库 2：手动修改
     */
    private Byte type;

    @Transient
    private String typeInfo;

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

}