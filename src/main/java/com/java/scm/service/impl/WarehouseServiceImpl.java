package com.java.scm.service.impl;

import com.java.scm.bean.Warehouse;
import com.java.scm.dao.WarehouseDao;
import com.java.scm.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hujunhui
 * @date 2020/6/26
 */
@Slf4j
@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Resource
    private WarehouseDao warehouseDao;

    @Override
    public List<Warehouse> getAllWarehouse() {
        return warehouseDao.selectAll();
    }
}
