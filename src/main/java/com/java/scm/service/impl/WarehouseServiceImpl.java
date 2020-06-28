package com.java.scm.service.impl;

import com.java.scm.bean.User;
import com.java.scm.bean.Warehouse;
import com.java.scm.dao.WarehouseDao;
import com.java.scm.service.WarehouseService;
import com.java.scm.util.RequestUtil;
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
}
