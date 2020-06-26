package com.java.scm.controller;

import com.java.scm.bean.Warehouse;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hujunhui
 * @date 2020/6/26
 */
@Slf4j
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    @Resource
    private WarehouseService warehouseService;

    @GetMapping("/list")
    public BaseResult list(){
        List<Warehouse> all = warehouseService.getAllWarehouse();
        return new BaseResult(all);
    }
}
