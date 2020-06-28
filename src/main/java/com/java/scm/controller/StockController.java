package com.java.scm.controller;

import com.java.scm.bean.Stock;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @PostMapping("/init")
    public BaseResult initStock(@RequestBody  Stock stock){
        return stockService.initStock(stock);
    }
    @PostMapping("/modifyStockInfo")
    public BaseResult modifyStockInfo(@RequestBody Stock stock){
        return stockService.modifyStockInfo(stock);
    }
    @PostMapping("/modifyStockCount")
    public BaseResult modifyStockCount(@RequestBody Stock stock){
        return stockService.modifyStockCount(stock);
    }
    @GetMapping("/list")
    public BaseResult listStock(String key, int pageNum, int pageSize){
        return stockService.listStock(key,pageNum,pageSize);
    }
    @GetMapping("/delete")
    public BaseResult deleteStock(Long id){
        return stockService.deleteStock(id);
    }
    @GetMapping("/get/{id}")
    public BaseResult getStockById(@PathVariable("id") Long id){
        return stockService.getStockById(id);
    }

    @GetMapping("detail")
    public BaseResult getChangeDetail(Long id, String startDate, String endDate, int pageNum, int pageSize){
        return stockService.getChangeDetail(id,startDate,endDate,pageNum,pageSize);
    }
}
