package com.java.scm.service;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.so.InoutStockSO;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-24 12:05
 */
public interface InoutStockService {

    /**
     * 出入库列表
     * @return
     */
    PageInfo<InoutStock> listInoutStock(InoutStockSO inoutStockSO);

    /**
     * 新增出入库
     */
    void insertInoutStock(InoutStock inoutStock);

}
