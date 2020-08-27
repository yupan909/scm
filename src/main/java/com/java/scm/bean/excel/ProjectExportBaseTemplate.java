package com.java.scm.bean.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

/**
 * 工程模型（导出）（普通仓库人员）
 *
 * @author yupan
 * @date 2020-06-25 23:34
 */
@Getter
@Setter
public class ProjectExportBaseTemplate {

    @Excel(name = "序号", orderNum = "0")
    private String num;

    @Excel(name = "客户名称", orderNum = "1", width = 30.0D)
    private String customer;

    @Excel(name = "工程名称", orderNum = "2", width = 30.0D)
    private String name;

    @Excel(name = "工程内容", orderNum = "3", width = 30.0D)
    private String content;

    @Excel(name = "工程进度", orderNum = "4", width = 15.0D)
    private String progress;

    @Excel(name = "状态", orderNum = "5")
    private String stateInfo;

}
