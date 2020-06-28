package com.java.scm.service;

import com.java.scm.bean.Warehouse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author hujunhui
 * @date 2020/6/26
 */
public interface WarehouseService {
    /**
     * 提取所有的仓库
     * @return
     */
    List<Warehouse> getAllWarehouse();

    /**
     * 查询仓库id对应名称集合
     * @return
     */
    Map<Integer, String> getWarehouseMap(List<Integer> ids);
}
