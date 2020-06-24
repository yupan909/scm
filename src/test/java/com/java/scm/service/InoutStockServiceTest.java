package com.java.scm.service;

import com.java.scm.bean.InoutStock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

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
     * 新增出入库
     */
    @Test
    public void insertInoutStock(){
        InoutStock inoutStock = new InoutStock();
        inoutStock.setWarehouseId(1);
        inoutStock.setProject("工程B");
        inoutStock.setProduct("产品2");
        inoutStock.setModel("10瓶/箱");
        inoutStock.setUnit("瓶");
        inoutStock.setCount(3);
        inoutStock.setHandle("余xx");
        inoutStock.setPrice(new BigDecimal("2"));
        inoutStockService.insertInoutStock(inoutStock);
    }
}
