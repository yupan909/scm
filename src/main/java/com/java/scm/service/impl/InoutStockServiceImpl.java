package com.java.scm.service.impl;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.dao.InoutStockDao;
import com.java.scm.service.InoutStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-24 12:05
 */
@Slf4j
@Service
public class InoutStockServiceImpl implements InoutStockService {

    @Autowired
    private InoutStockDao inoutStockDao;


    @Override
    public PageInfo<InoutStock> listInoutStock(InoutStockSO inoutStockSO) {
        return null;
    }

    @Override
    public void insertInoutStock(InoutStock inoutStock) {
        inoutStockDao.insertSelective(inoutStock);
    }
}
