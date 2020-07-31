package com.java.scm.bean.so;

import com.java.scm.bean.base.PageCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 库存查询
 *
 * @author yupan
 * @date 2020-06-24 12:19
 */
@Getter
@Setter
public class StockSO extends PageCondition {

    /**
     * 仓库id
     */
    private String warehouseId;

    /**
     * 物资名称
     */
    private String product;

    /**
     * 物资型号
     */
    private String model;

}
