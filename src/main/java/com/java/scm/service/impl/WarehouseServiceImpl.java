package com.java.scm.service.impl;

import com.java.scm.bean.User;
import com.java.scm.bean.Warehouse;
import com.java.scm.dao.WarehouseDao;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.RequestUtil;
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

    private static Byte ADMIN = (byte) 1 ;

    @Resource
    private WarehouseDao warehouseDao;

    @Override
    public List<Warehouse> getAllWarehouse() {
        User user = RequestUtil.getCurrentUser();
        if(ADMIN.equals(user.getAdmin())){
            return warehouseDao.selectAll();
        }else{
            Warehouse query = new Warehouse();
            query.setId(user.getWarehouseId());
            return warehouseDao.select(query);
        }

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
