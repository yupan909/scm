package com.java.scm.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * 出库单模型（导出）
 *
 * @author yupan
 * @date 2020-06-25 23:34
 */
@Getter
@Setter
public class OutStockExportTemplate {

    @Excel(name = "序号", orderNum = "0")
    private String num;

    @Excel(name = "工程名称", orderNum = "1", width = 40.0D)
    private String project;

    @Excel(name = "物资名称", orderNum = "2", width = 30.0D)
    private String product;

    @Excel(name = "物资型号", orderNum = "3", width = 20.0D)
    private String model;

    @Excel(name = "单位", orderNum = "4")
    private String unit;

    @Excel(name = "数量", orderNum = "5")
    private String count;

    @Excel(name = "经手人", orderNum = "6")
    private String handle;

    @Excel(name = "备注", orderNum = "7")
    private String remark;

    @Excel(name = "时间", orderNum = "8",  width = 20.0D)
    private String createTime;

}
