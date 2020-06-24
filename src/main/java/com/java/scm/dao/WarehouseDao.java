package com.java.scm.dao;

import com.java.scm.bean.Warehouse;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库
 *
 * @author yupan
 * @date 2020-06-23 22:08
 */
@Mapper
public interface WarehouseDao extends TkMapper<Warehouse> {

}