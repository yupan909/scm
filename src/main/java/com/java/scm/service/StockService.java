package com.java.scm.service;

import com.java.scm.bean.InoutStock;
import com.java.scm.bean.Stock;
import com.java.scm.bean.base.BaseResult;

import java.util.List;

/**
 * 库存信息服务
 * @author hujunhui
 * @date 2020/6/28
 */
public interface StockService {

    /**
     * 初始化库存
     * @param stock
     * @return
     */
    BaseResult initStock(Stock stock);

    /**
     * 修改库存基本信息
     * @param stock
     * @return
     */
    BaseResult modifyStockInfo(Stock stock);

    /**
     * 修改库存数量
     * @param stock
     * @return
     */
    BaseResult modifyStockCount(Stock stock);

    /**
     * 列表展示
     * @param key
     * @param pageNum
     * @param pageSize
     * @return
     */
    BaseResult listStock(String key,int pageNum,int pageSize);


    /**
     * 根据id 删除库存信息
     * @param id
     * @return
     */
    BaseResult deleteStock(Long id);

    /**
     * 根据id 获取库存信息
     * @param id
     * @return
     */
    BaseResult getStockById(Long id);


    /**
     * 获取变更记录
     * @param id
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     */
    BaseResult getChangeDetail(Long id,String startDate,String endDate,int pageNum,int pageSize);

    /**
     * 批量变更库存
     */
    void insertStock(List<InoutStock> inoutStockList);


}
