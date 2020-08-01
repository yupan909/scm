package com.java.scm.bean.so;

import com.java.scm.bean.base.PageCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 库存记录查询
 *
 * @author yupan
 * @date 2020-06-24 12:19
 */
@Getter
@Setter
public class StockRecordSO extends PageCondition {

    /**
     * 库存id
     */
    private String stockId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
