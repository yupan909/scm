package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 库存id
     */
    private Long stockId;

    /**
     * 工程名称
     */
    private String project;

    /**
     * 变更数量
     */
    private Integer count;

    /**
     * 变更类型 0：入库 1：出库 2：手动修改
     */
    private Byte type;

    /**
     * 出入库id
     */
    private Long inoutStockId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    @Transient
    private String typeInfo;

}