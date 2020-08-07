package com.java.scm.service;

import com.java.scm.bean.Warehouse;

import java.util.List;
import java.util.Map;

/**
 * @author hujunhui
 * @date 2020/6/26
 */
public interface WarehouseService {
    /**
     * 获取所有的仓库
     * @return
     */
    List<Warehouse> getAllWarehouse();

    /**
     * 查询仓库id对应名称集合
     * @return
     */
    Map<String, String> getWarehouseMap(List<String> ids);
}
