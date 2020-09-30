package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Stock;
import com.java.scm.bean.StockRecord;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.excel.StockExportTemplate;
import com.java.scm.bean.excel.StockImportTemplate;
import com.java.scm.bean.so.StockRecordSO;
import com.java.scm.bean.so.StockSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.service.StockService;
import com.java.scm.util.DateUtils;
import com.java.scm.util.RequestUtil;
import com.java.scm.util.StringUtil;
import com.java.scm.util.excel.ExcelTypeEnum;
import com.java.scm.util.excel.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@RestController
@RequestMapping("/stock")
@Slf4j
public class StockController {

    @Resource
    private StockService stockService;

    /**
     * 初始化库存
     * @return
     */
    @PostMapping("/init")
    public BaseResult initStock(@RequestBody Stock stock){
        stockService.initStock(stock);
        return BaseResult.successResult();
    }

    /**
     * 修改库存基本信息
     * @param stock
     * @return
     */
    @PostMapping("/modifyStockInfo")
    public BaseResult modifyStockInfo(@RequestBody Stock stock){
        stockService.modifyStockInfo(stock);
        return BaseResult.successResult();
    }

    /**
     * 修改库存数量
     * @param stock
     * @return
     */
    @PostMapping("/modifyStockCount")
    public BaseResult modifyStockCount(@RequestBody Stock stock){
        stockService.modifyStockCount(stock);
        return BaseResult.successResult();
    }

    /**
     * 列表展示
     * @return
     */
    @PostMapping("/list")
    public BaseResult listStock(@RequestBody StockSO stockSO){
        if (StringUtil.isEmpty(stockSO.getWarehouseId())) {
            stockSO.setWarehouseId(RequestUtil.getWarehouseId());
        }
        PageInfo<Stock> pageInfo = stockService.listStock(stockSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 根据id 删除库存信息
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public BaseResult deleteStock(String id){
        stockService.deleteStock(id);
        return BaseResult.successResult();
    }

    /**
     * 根据id 获取库存信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public BaseResult getStockById(@PathVariable("id") String id){
        Stock stock = stockService.getStockById(id);
        return new BaseResult(stock);
    }

    /**
     * 获取库存变更记录
     * @return
     */
    @PostMapping("/detail")
    public BaseResult getChangeDetail(@RequestBody StockRecordSO stockRecordSO){
        PageInfo<StockRecord> pageInfo = stockService.getChangeDetail(stockRecordSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 删除库存变更记录
     * @param id
     * @return
     */
    @GetMapping("/deteleDetail/{id}")
    public BaseResult deteleDetail(@PathVariable("id") String id){
        stockService.deteleDetail(id);
        return BaseResult.successResult();
    }

    /**
     * 导入
     * @throws Exception
     */
    @PostMapping("/import")
    public BaseResult importInoutStock(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.getOriginalFilename().indexOf(ExcelTypeEnum.XLS.getValue()) == -1 && file.getOriginalFilename().indexOf(ExcelTypeEnum.XLSX.getValue()) == -1) {
            throw new BusinessException("导入文件类型不正确，只能导入.xls和.xlsx后缀的文件！");
        }
        // 获取导入的excel数据
        List<StockImportTemplate> importList = ExcelUtils.importExcel(file, 1,1, StockImportTemplate.class);
        if (CollectionUtils.isEmpty(importList)) {
            throw new BusinessException("导入excel数据为空！");
        }
        stockService.importStock(importList);
        return BaseResult.successResult();
    }

    /**
     * 导出模版
     * @throws Exception
     */
    @GetMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        ExcelUtils.exportExcel(new ArrayList<>(), StockImportTemplate.class, "库存管理", "库存模版", response);
    }

    /**
     * 导出库存
     * @throws Exception
     */
    @GetMapping("/exportStock")
    public void exportStock(@RequestParam("warehouseId") String warehouseId,
                            @RequestParam("product") String product,
                            @RequestParam("model") String model,
                            HttpServletResponse response) throws Exception {
        // 查询数据
        StockSO stockSO = new StockSO();
        stockSO.setWarehouseId(warehouseId);
        if (StringUtil.isNotEmpty(product)) {
            stockSO.setProduct(URLDecoder.decode(product, "utf-8"));
        }
        if (StringUtil.isNotEmpty(model)) {
            stockSO.setModel(URLDecoder.decode(model, "utf-8"));
        }
        PageInfo<Stock> pageInfo = stockService.listStock(stockSO);

        List<StockExportTemplate> exportList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            for(int i = 0; i < pageInfo.getList().size(); i++) {
                Stock stock = pageInfo.getList().get(i);
                StockExportTemplate template = new StockExportTemplate();
                BeanUtils.copyProperties(stock, template);
                template.setNum(String.valueOf(i+1));
                template.setCount(stock.getCount() != null ? String.valueOf(stock.getCount()) : "");
                template.setCreateTime(DateUtils.formatDateTime(stock.getCreateTime()));
                exportList.add(template);
            }
        }
        ExcelUtils.exportExcel(exportList, StockExportTemplate.class, "库存管理", "库存管理", response);
    }
}
