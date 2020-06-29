package com.java.scm.util.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

/**
 * 自定义Excel样式
 *
 * @author yupan
 * @date 2020-06-23 17:47
 */
public class MyExcelStyle extends AbstractExcelExportStyler implements IExcelExportStyler {

    public MyExcelStyle(Workbook workbook) {
        super.createStyles(workbook);
    }

    /**
     * 列表头样式
     */
    @Override
    public CellStyle getHeaderStyle(short i) {
        CellStyle style = getBaseCellStyle(workbook,false);
        style.setFont(getFont(workbook, (short)15, true));
        return style;
    }

    /**
     * 每列标题样式
     */
    @Override
    public CellStyle getTitleStyle(short i) {
        CellStyle style = getBaseCellStyle(workbook,true);
        style.setFont(getFont(workbook, (short)13, true));
        return style;
    }

    /**
     * 单元格样式
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        CellStyle style = getBaseCellStyle(workbook,true);
        style.setFont(getFont(workbook, (short)12, false));
        return style;
    }

    /**
     * 间隔行
     */
    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = getBaseCellStyle(workbook, true);
        style.setFont(getFont(workbook, (short)12, false));
        style.setDataFormat(STRING_FORMAT);
        return style;
    }

    /**
     * 单行
     */
    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = getBaseCellStyle(workbook, true);
        style.setFont(getFont(workbook, (short)12, false));
        style.setDataFormat(STRING_FORMAT);
        return style;
    }

    /**
     * 基础样式
     * @param workbook
     * @param isBorder 是否需要边框
     * @return
     */
    private CellStyle getBaseCellStyle(Workbook workbook, boolean isBorder) {
        CellStyle style = workbook.createCellStyle();
        if (isBorder) {
            //下边框
            style.setBorderBottom(BorderStyle.THIN);
            //左边框
            style.setBorderLeft(BorderStyle.THIN);
            //上边框
            style.setBorderTop(BorderStyle.THIN);
            //右边框
            style.setBorderRight(BorderStyle.THIN);
        }
        //水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        //上下居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置自动换行
        style.setWrapText(true);
        return style;
    }

    /**
     * 字体样式
     *
     * @param size   字体大小
     * @param isBold 是否加粗
     * @return
     */
    private Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();
        //字体样式
        font.setFontName("宋体");
        //是否加粗
        font.setBold(isBold);
        //字体大小
        font.setFontHeightInPoints(size);
        return font;
    }
}
