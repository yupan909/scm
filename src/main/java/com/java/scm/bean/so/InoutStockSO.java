package com.java.scm.bean.so;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.scm.bean.base.PageCondition;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 出入库查询
 *
 * @author yupan
 * @date 2020-06-24 12:19
 */
@Getter
@Setter
public class InoutStockSO extends PageCondition {

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
     * 类别 0：入库 1：出库
     */
    private Byte type;

    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
