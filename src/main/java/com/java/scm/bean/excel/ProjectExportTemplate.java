package com.java.scm.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * 工程模型（导出）
 *
 * @author yupan
 * @date 2020-06-25 23:34
 */
@Getter
@Setter
public class ProjectExportTemplate {

    @Excel(name = "序号", orderNum = "0")
    private String num;

    @Excel(name = "工程名称", orderNum = "1", width = 40.0D)
    private String name;

    @Excel(name = "合同金额", orderNum = "2", width = 15.0D)
    private String contractMoney;

    @Excel(name = "结算金额", orderNum = "3", width = 15.0D)
    private String finalMoney;

    @Excel(name = "收入金额", orderNum = "4", width = 15.0D)
    private String inMoney;

    @Excel(name = "支出金额", orderNum = "5", width = 15.0D)
    private String outMoney;

    @Excel(name = "合计金额", orderNum = "6", width = 15.0D)
    private String sumMoney;


}
