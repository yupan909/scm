package com.java.scm.service.impl;

import com.java.scm.bean.Warehouse;
import com.java.scm.dao.WarehouseDao;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<Integer, String> getWarehouseMap(List<Integer> ids) {
        AssertUtils.notEmpty(ids, "仓库id不能为空");
        Example example = new Example(Warehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);
        List<Warehouse> warehouseList = warehouseDao.selectByExample(example);
        if (CollectionUtils.isEmpty(warehouseList)) {
            return Collections.EMPTY_MAP;
        }
        Map<Integer, String> warehouseMap = new HashMap<>();
        warehouseList.forEach(warehouse -> {
            warehouseMap.put(warehouse.getId(), warehouse.getName());
        });
        return warehouseMap;
    }
}
