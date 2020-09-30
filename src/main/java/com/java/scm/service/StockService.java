package com.java.scm.service;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.Stock;
import com.java.scm.bean.StockRecord;
import com.java.scm.bean.excel.StockImportTemplate;
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
    void initStock(Stock stock);

    /**
     * 修改库存基本信息
     * @param stock
     * @return
     */
    void modifyStockInfo(Stock stock);

    /**
     * 修改库存数量
     * @param stock
     * @return
     */
    void modifyStockCount(Stock stock);

    /**
     * 列表展示
     * @return
     */
    PageInfo<Stock> listStock(StockSO stockSO);

    /**
     * 根据id 删除库存信息
     * @param id
     * @return
     */
    void deleteStock(String id);

    /**
     * 根据id 获取库存信息
     * @param id
     * @return
     */
    Stock getStockById(String id);

    /**
     * 获取变更记录
     * @return
     */
    PageInfo<StockRecord> getChangeDetail(StockRecordSO stockRecordSO);

    /**
     * 删除变更记录
     */
    void deteleDetail(String id);

    /**
     * 出入库变更库存
     */
    void changeStock(List<InoutStock> inoutStockList);

    /**
     * 按条件查询库存Id
     * @return
     */
    String getStockBySelective(Stock stock);

    /**
     * 导入库存
     */
    void importStock(List<StockImportTemplate> importList);

}
