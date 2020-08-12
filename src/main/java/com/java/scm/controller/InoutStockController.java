package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.excel.*;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.enums.InoutStockTypeEnum;
import com.java.scm.service.InoutStockService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.DateUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import com.java.scm.util.excel.ExcelTypeEnum;
import com.java.scm.util.excel.ExcelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-25 23:49
 */
@RestController
@RequestMapping("/inoutStock")
public class InoutStockController {

    @Autowired
    private InoutStockService inoutStockService;

    /**
     * 查询出入库列表
     * @return
     */
    @PostMapping("/list")
    public BaseResult listInoutStock(@RequestBody InoutStockSO inoutStockSO) {
        inoutStockSO.setWarehouseId(RequestUtil.getWarehouseId());
        PageInfo<InoutStock> pageInfo = inoutStockService.listInoutStock(inoutStockSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 新增出入库
     * @return
     */
    @PostMapping("/save")
    public BaseResult listInoutStock(@RequestBody InoutStock inoutStock) {
        inoutStockService.saveInoutStock(inoutStock);
        return BaseResult.successResult();
    }

    /**
     * 导入
     * @throws Exception
     */
    @PostMapping("/import/{type}")
    public BaseResult importInoutStock(@RequestParam("file")MultipartFile file, @PathVariable("type") Byte type) throws Exception {
        AssertUtils.notNull(type, "出入库类型不能为空！");
        if (file.getOriginalFilename().indexOf(ExcelTypeEnum.XLS.getValue()) == -1 && file.getOriginalFilename().indexOf(ExcelTypeEnum.XLSX.getValue()) == -1) {
            throw new BusinessException("导入文件类型不正确，只能导入.xls和.xlsx后缀的文件！");
        }

        // 获取导入的excel数据
        List<InStockImportTemplate> importList = ExcelUtils.importExcel(file, 1,1, InStockImportTemplate.class);
        if (CollectionUtils.isEmpty(importList)) {
            throw new BusinessException("导入excel数据为空！");
        }

        Byte inoutStockType = InoutStockTypeEnum.入库.getType();
        if (Objects.equals(type, InoutStockTypeEnum.出库.getType())) {
            inoutStockType = InoutStockTypeEnum.出库.getType();
        }

        inoutStockService.importInoutStock(importList, inoutStockType);
        return BaseResult.successResult();
    }

    /**
     * 导出模版
     * @throws Exception
     */
    @GetMapping("/exportTemplate/{type}")
    public void exportTemplate(HttpServletResponse response, @PathVariable("type") Byte type) throws Exception {
        AssertUtils.notNull(type, "出入库类型不能为空！");
        if (Objects.equals(type, InoutStockTypeEnum.入库.getType())) {
            ExcelUtils.exportExcel(new ArrayList<>(), InStockImportTemplate.class, "入库单", "入库单模版", response);
        } else {
            ExcelUtils.exportExcel(new ArrayList<>(), OutStockImportTemplate.class, "出库单", "出库单模版", response);
        }
    }

    /**
     * 导出出入库管理
     * @throws Exception
     */
    @GetMapping("/exportInoutStock")
    public void exportInoutStock(@RequestParam("type") Byte type,
                                       @RequestParam("project") String project,
                                       @RequestParam("product") String product,
                                       @RequestParam("model") String model,
                                       @RequestParam("source") String source,
                                       @RequestParam("startTime") String startTime,
                                       @RequestParam("endTime") String endTime,
                                       HttpServletResponse response) throws Exception {
        AssertUtils.notNull(type, "出入库类型不能为空！");
        // 查询数据
        InoutStockSO inoutStockSO = getInoutStockSO(type, project, product, model, source, startTime, endTime, null);
        PageInfo<InoutStock> pageInfo = inoutStockService.listInoutStock(inoutStockSO);

        if (Objects.equals(type, InoutStockTypeEnum.入库.getType())) {
            List<InStockExportTemplate> inExportList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pageInfo.getList())) {
                for(int i = 0; i < pageInfo.getList().size(); i++) {
                    InoutStock inoutStock = pageInfo.getList().get(i);
                    InStockExportTemplate template = new InStockExportTemplate();
                    BeanUtils.copyProperties(inoutStock, template);
                    template.setNum(String.valueOf(i+1));
                    template.setCount(inoutStock.getCount() != null ? String.valueOf(inoutStock.getCount()) : "");
                    template.setPrice(inoutStock.getPrice() != null ? inoutStock.getPrice().stripTrailingZeros().toPlainString() : "");
                    template.setCreateTime(DateUtils.formatDateTime(inoutStock.getCreateTime()));
                    inExportList.add(template);
                }
            }
            ExcelUtils.exportExcel(inExportList, InStockExportTemplate.class, "入库管理", "入库管理", response);
        } else {
            List<OutStockExportTemplate> outExportList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pageInfo.getList())) {
                for(int i = 0; i < pageInfo.getList().size(); i++) {
                    InoutStock inoutStock = pageInfo.getList().get(i);
                    OutStockExportTemplate template = new OutStockExportTemplate();
                    BeanUtils.copyProperties(inoutStock, template);
                    template.setNum(String.valueOf(i+1));
                    template.setCount(inoutStock.getCount() != null ? String.valueOf(inoutStock.getCount()) : "");
                    template.setCreateTime(DateUtils.formatDateTime(inoutStock.getCreateTime()));
                    outExportList.add(template);
                }
            }
            ExcelUtils.exportExcel(outExportList, OutStockExportTemplate.class, "出库管理", "出库管理", response);
        }
    }

    /**
     * 导出出入库报表
     * @throws Exception
     */
    @GetMapping("/exportInoutStockReport")
    public void exportInoutStockReport(@RequestParam("type") Byte type,
                                       @RequestParam("warehouseId") String warehouseId,
                                       @RequestParam("project") String project,
                                       @RequestParam("product") String product,
                                       @RequestParam("model") String model,
                                       @RequestParam("source") String source,
                                       @RequestParam("startTime") String startTime,
                                       @RequestParam("endTime") String endTime,
                                       HttpServletResponse response) throws Exception {
        // 查询数据
        InoutStockSO inoutStockSO = getInoutStockSO(type, project, product, model, source, startTime, endTime, warehouseId);
        PageInfo<InoutStock> pageInfo = inoutStockService.listInoutStock(inoutStockSO);

        List<InoutStockReportTemplate> exportList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            for(int i = 0; i < pageInfo.getList().size(); i++) {
                InoutStock inoutStock = pageInfo.getList().get(i);
                InoutStockReportTemplate template = new InoutStockReportTemplate();
                BeanUtils.copyProperties(inoutStock, template);
                template.setNum(String.valueOf(i+1));
                template.setType(inoutStock.getTypeText());
                template.setWarehouse(inoutStock.getWarehouseName());
                template.setCount(inoutStock.getCount() != null ? String.valueOf(inoutStock.getCount()) : "");
                template.setPrice(inoutStock.getPrice() != null ? inoutStock.getPrice().stripTrailingZeros().toPlainString() : "");
                template.setCreateTime(DateUtils.formatDateTime(inoutStock.getCreateTime()));
                exportList.add(template);
            }
        }
        ExcelUtils.exportExcel(exportList, InoutStockReportTemplate.class, "出入库报表", "出入库报表", response);
    }

    /**
     * 组装出入库查询对象
     */
    private InoutStockSO getInoutStockSO(Byte type, String project, String product, String model, String source, String startTime, String endTime, String warehouseId) throws UnsupportedEncodingException {
        InoutStockSO inoutStockSO = new InoutStockSO();
        inoutStockSO.setType(type);
        if (warehouseId == null) {
            inoutStockSO.setWarehouseId(RequestUtil.getWarehouseId());
        }
        inoutStockSO.setWarehouseId(warehouseId);
        if (StringUtil.isNotEmpty(project)) {
            inoutStockSO.setProject(URLDecoder.decode(project, "utf-8"));
        }
        if (StringUtil.isNotEmpty(product)) {
            inoutStockSO.setProduct(URLDecoder.decode(product, "utf-8"));
        }
        if (StringUtil.isNotEmpty(model)) {
            inoutStockSO.setModel(URLDecoder.decode(model, "utf-8"));
        }
        if (StringUtil.isNotEmpty(source)) {
            inoutStockSO.setSource(URLDecoder.decode(source, "utf-8"));
        }
        inoutStockSO.setStartTime(startTime);
        inoutStockSO.setEndTime(endTime);
        return inoutStockSO;
    }

    /**
     * 工程物资统计
     * @return
     */
    @PostMapping("/listByProject")
    public BaseResult listInoutStockGroupByProject(@RequestBody InoutStockSO inoutStockSO) {
        PageInfo<InoutStock> pageInfo = inoutStockService.listInoutStockGroupByProject(inoutStockSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 工程物资统计
     * @throws Exception
     */
    @GetMapping("/exportInoutStockReportByProject")
    public void exportInoutStockReport(@RequestParam("project") String project,
                                       @RequestParam("product") String product,
                                       @RequestParam("model") String model,
                                       HttpServletResponse response) throws Exception {
        // 查询数据
        InoutStockSO inoutStockSO = getInoutStockSO(null, project, product, model, null, null, null, null);
        PageInfo<InoutStock> pageInfo = inoutStockService.listInoutStockGroupByProject(inoutStockSO);

        List<ProjectReportTemplate> exportList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            for(int i = 0; i < pageInfo.getList().size(); i++) {
                InoutStock inoutStock = pageInfo.getList().get(i);
                ProjectReportTemplate template = new ProjectReportTemplate();
                BeanUtils.copyProperties(inoutStock, template);
                template.setNum(String.valueOf(i+1));
                template.setInCount(inoutStock.getInCount() != null ? String.valueOf(inoutStock.getInCount()) : "");
                template.setOutCount(inoutStock.getOutCount() != null ? String.valueOf(inoutStock.getOutCount()) : "");
                exportList.add(template);
            }
        }
        ExcelUtils.exportExcel(exportList, ProjectReportTemplate.class, "工程物资统计", "工程物资统计", response);
    }


}
