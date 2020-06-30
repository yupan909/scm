package com.java.scm.util.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel文档导入导出工具类
 *
 * @author yupan
 * @date 2020-06-26 17:50
 */
public class ExcelUtils {

    /**
     * 导出
     * @param list           数据
     * @param clazz          pojo类型
     * @param title          标题
     * @param fileName       文件名称
     * @param response
     */
    public static void exportExcel(List<?> list, Class<?> clazz, String title, String fileName, HttpServletResponse response) throws Exception {
        ExportParams exportParams = new ExportParams(title, null, ExcelType.XSSF);
        // 自定义样式
        exportParams.setStyle(MyExcelStyle.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, clazz, list);
        try{
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName  + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workbook.write(response.getOutputStream());
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    /**
     * 导入
     * @param file       上传的文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param clazz      pojo类型
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> clazz) throws Exception {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        return ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
    }
}
