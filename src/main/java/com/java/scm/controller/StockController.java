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
     * @param key
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public BaseResult listStock(String key, int pageNum, int pageSize){
        return stockService.listStock(key,pageNum,pageSize);
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
     * 获取变更记录
     * @param id
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("detail")
    public BaseResult getChangeDetail(String id, String startDate, String endDate, int pageNum, int pageSize){
        return stockService.getChangeDetail(id,startDate,endDate,pageNum,pageSize);
    }
}
