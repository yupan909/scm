package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.InoutStock;
import com.java.scm.bean.so.InoutStockSO;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-24 12:04
 */
@Mapper
public interface InoutStockMapper extends TkMapper<InoutStock> {

    /**
     * 出入库列表
     * @return
     */
    Page<InoutStock> listInoutStock(InoutStockSO inoutStockSO);

    /**
     * 工程出入库统计
     * @return
     */
    Page<InoutStock> listInoutStockGroupbyProject(InoutStockSO inoutStockSO);
}
