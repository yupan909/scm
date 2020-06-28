package com.java.scm.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * 出入库报表模型
 *
 * @author yupan
 * @date 2020-06-25 23:34
 */
@Getter
@Setter
public class InoutStockReportTemplate {

    @Excel(name = "序号", orderNum = "0")
    private String num;

    @Excel(name = "类型", orderNum = "1")
    private String type;

    @Excel(name = "仓库", orderNum = "2", width = 40.0D)
    private String warehouse;

    @Excel(name = "工程名称", orderNum = "3", width = 35.0D)
    private String project;

    @Excel(name = "物资名称", orderNum = "4", width = 30.0D)
    private String product;

    @Excel(name = "物资型号", orderNum = "5", width = 15.0D)
    private String model;

    @Excel(name = "单位", orderNum = "6")
    private String unit;

    @Excel(name = "数量", orderNum = "7")
    private String count;

    @Excel(name = "物资单价（元）", orderNum = "8", width = 15.0D)
    private String price;

    @Excel(name = "物资来源", orderNum = "9", width = 15.0D)
    private String source;

    @Excel(name = "经手人", orderNum = "10")
    private String handle;

    @Excel(name = "备注", orderNum = "11")
    private String remark;

}
