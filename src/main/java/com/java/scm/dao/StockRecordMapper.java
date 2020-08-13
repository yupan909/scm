package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.StockRecord;
import com.java.scm.bean.so.StockRecordSO;
import com.java.scm.tk.TkMapper;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
public interface StockRecordMapper extends TkMapper<StockRecord> {

    /**
     * 库存明细列表
     * @return
     */
    Page<StockRecord> listStockRecord(StockRecordSO stockRecordSO);
}
