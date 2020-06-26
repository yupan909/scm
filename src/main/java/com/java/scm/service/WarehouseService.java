package com.java.scm.service;

import com.java.scm.bean.Warehouse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
}
