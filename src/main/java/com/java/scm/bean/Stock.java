package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 库存表
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock")
public class Stock {

    /**
     * 库存id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 仓库id
     */
    private Integer warehouseId;

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
     * 库存数量
     */
    private Integer count;

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