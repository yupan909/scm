package com.java.scm.service;

import com.java.scm.bean.InoutStock;
import com.java.scm.bean.Stock;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.so.StockRecordSO;
import com.java.scm.bean.so.StockSO;

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
     * @return
     */
    BaseResult listStock(StockSO stockSO);

    /**
     * 根据id 删除库存信息
     * @param id
     * @return
     */
    BaseResult deleteStock(String id);

    /**
     * 根据id 获取库存信息
     * @param id
     * @return
     */
    BaseResult getStockById(String id);

    /**
     * 获取变更记录
     * @return
     */
    BaseResult getChangeDetail(StockRecordSO stockRecordSO);

    /**
     * 出入库变更库存
     */
    void changeStock(List<InoutStock> inoutStockList);

    /**
     * 按条件查询库存Id
     * @return
     */
    String getStockBySelective(Stock stock);

}
