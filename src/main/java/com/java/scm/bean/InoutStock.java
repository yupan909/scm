package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 仓库id
     */
    private Integer warehouseId;

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

    private String unit;

    private Integer count;

    private String handle;

    private String source;

    private BigDecimal price;

    private String remark;

    private Boolean type;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createUser;
}