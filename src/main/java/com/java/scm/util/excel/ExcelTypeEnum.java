package com.java.scm.util.excel;

/**
 * excel文件类型
 *
 * @author yupan
 * @date 2020-06-23 14:05
 */
public enum ExcelTypeEnum {

    XLS(".xls"), XLSX(".xlsx");

    private String value;

    ExcelTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
