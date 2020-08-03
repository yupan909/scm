package com.java.scm.bean.so;

import com.java.scm.bean.base.PageCondition;
import lombok.Getter;
import lombok.Setter;

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
    private String warehouseId;

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
     * 物资来源
     */
    private String source;

    /**
     * 类别 0：入库 1：出库
     */
    private Byte type;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
