package com.java.scm.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * 库存模型（导出）
 *
 * @author yupan
 * @date 2020-06-25 23:34
 */
@Getter
@Setter
public class StockExportTemplate {

    @Excel(name = "序号", orderNum = "0")
    private String num;

    @Excel(name = "物资名称", orderNum = "1", width = 40.0D)
    private String product;

    @Excel(name = "物资型号", orderNum = "2", width = 30.0D)
    private String model;

    @Excel(name = "单位", orderNum = "3")
    private String unit;

    @Excel(name = "库存数量", orderNum = "4", width = 15.0D)
    private String count;

    @Excel(name = "所属仓库", orderNum = "5", width = 30.0D)
    private String warehouseName;

    @Excel(name = "创建时间", orderNum = "6",  width = 20.0D)
    private String createTime;

}
