package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.StockRecord;
import com.java.scm.bean.so.StockRecordSO;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Mapper
public interface StockRecordDao  extends TkMapper<StockRecord> {

    /**
     * 库存明细列表
     * @return
     */
    Page<StockRecord> listStockRecord(StockRecordSO stockRecordSO);
}
