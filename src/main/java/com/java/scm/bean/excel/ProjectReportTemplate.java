package com.java.scm.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * 工程物资统计模型
 *
 * @author yupan
 * @date 2020-06-25 23:34
 */
@Getter
@Setter
public class ProjectReportTemplate {

    @Excel(name = "序号", orderNum = "0")
    private String num;

    @Excel(name = "工程名称", orderNum = "1", width = 35.0D)
    private String project;

    @Excel(name = "物资名称", orderNum = "2", width = 30.0D)
    private String product;

    @Excel(name = "物资型号", orderNum = "3", width = 15.0D)
    private String model;

    @Excel(name = "单位", orderNum = "4")
    private String unit;

    @Excel(name = "入库数量", orderNum = "5")
    private String inCount;

    @Excel(name = "出库数量", orderNum = "6")
    private String outCount;

}
