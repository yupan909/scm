package com.java.scm.service;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.excel.InStockImportTemplate;
import com.java.scm.bean.so.InoutStockSO;

import java.util.List;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-24 12:05
 */
public interface InoutStockService {

    /**
     * 查询出入库列表
     * @return
     */
    PageInfo<InoutStock> listInoutStock(InoutStockSO inoutStockSO);

    /**
     * 导入出入库
     */
    void importInoutStock(List<InStockImportTemplate> importList, Byte inoutStockType);

    /**
     * 新增出入库
     */
    void saveInoutStock(InoutStock inoutStock);

    /**
     * 工程出入库统计
     * @return
     */
    PageInfo<InoutStock> listInoutStockGroupByProject(InoutStockSO inoutStockSO);

    /**
     * 删除出入库
     */
    void deleteInoutStock(String id);

}
