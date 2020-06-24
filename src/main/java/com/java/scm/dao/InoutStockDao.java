package com.java.scm.dao;

import com.java.scm.bean.InoutStock;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020-06-24 12:04
 */
@Mapper
public interface InoutStockDao extends TkMapper<InoutStock> {
}
