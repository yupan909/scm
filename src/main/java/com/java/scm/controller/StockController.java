package com.java.scm.controller;

import com.java.scm.bean.Stock;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.excel.StockImportTemplate;
import com.java.scm.bean.so.StockRecordSO;
import com.java.scm.bean.so.StockSO;
import com.java.scm.service.StockService;
import com.java.scm.util.excel.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

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
        return stockService.initStock(stock);
    }

    /**
     * 修改库存基本信息
     * @param stock
     * @return
     */
    @PostMapping("/modifyStockInfo")
    public BaseResult modifyStockInfo(@RequestBody Stock stock){
        return stockService.modifyStockInfo(stock);
    }

    /**
     * 修改库存数量
     * @param stock
     * @return
     */
    @PostMapping("/modifyStockCount")
    public BaseResult modifyStockCount(@RequestBody Stock stock){
        return stockService.modifyStockCount(stock);
    }

    /**
     * 列表展示
     * @return
     */
    @PostMapping("/list")
    public BaseResult listStock(@RequestBody StockSO stockSO){
        return stockService.listStock(stockSO);
    }

    /**
     * 根据id 删除库存信息
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public BaseResult deleteStock(String id){
        return stockService.deleteStock(id);
    }

    /**
     * 根据id 获取库存信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public BaseResult getStockById(@PathVariable("id") String id){
        return stockService.getStockById(id);
    }

    /**
     * 获取库存变更记录
     * @return
     */
    @PostMapping("detail")
    public BaseResult getChangeDetail(@RequestBody StockRecordSO stockRecordSO){
        return stockService.getChangeDetail(stockRecordSO);
    }

    /**
     * 导出模版
     * @throws Exception
     */
    @GetMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        ExcelUtils.exportExcel(new ArrayList<>(), StockImportTemplate.class, "库存管理", "库存模版", response);
    }
}
