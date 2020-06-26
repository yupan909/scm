package com.java.scm.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.enums.InoutStockTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;

/**
 * 出入库测试
 *
 * @author yupan
 * @date 2020-06-24 12:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InoutStockServiceTest {

    @Autowired
    private InoutStockService inoutStockService;

    /**
     * 查询出入库列表
     */
    @Test
    public void listInoutStock() throws Exception{
        InoutStockSO inoutStockSO = new InoutStockSO();
        inoutStockSO.setWarehouseId(1);
        inoutStockSO.setType(InoutStockTypeEnum.入库.getType());
        inoutStockSO.setProject("A");
        inoutStockSO.setProduct("5");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        inoutStockSO.setStartTime(sdf.parse("2020-06-24"));
        inoutStockSO.setPageNum(1);
        inoutStockSO.setPageSize(2);
        PageInfo<InoutStock> pageInfo = inoutStockService.listInoutStock(inoutStockSO);
        System.out.println(JSON.toJSONString(pageInfo));
    }

    /**
     * 新增出入库
     */
    @Test
    public void insertInoutStock(){
        InoutStock inoutStock = new InoutStock();
        inoutStock.setWarehouseId(2);
        inoutStock.setProject("工程C");
        inoutStock.setProduct("产品7");
        inoutStock.setModel("15瓶/箱");
        inoutStock.setUnit("瓶");
        inoutStock.setCount(15);
        inoutStock.setHandle("余xx");
        inoutStock.setType(InoutStockTypeEnum.出库.getType());
        inoutStock.setCreateUser("张三");
        Long id = inoutStockService.insertInoutStock(inoutStock);
        System.out.println("出入库id:" + id);
    }

}
