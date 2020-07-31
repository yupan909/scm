package com.java.scm.dao;

import com.java.scm.bean.Stock;
import com.java.scm.bean.so.StockSO;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Mapper
public interface StockDao extends TkMapper<Stock> {

    /**
     * 库存列表
     * @return
     */
    List<Stock> listStock(StockSO stockSO);
}
