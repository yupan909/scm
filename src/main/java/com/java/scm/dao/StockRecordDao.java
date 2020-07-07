package com.java.scm.dao;

import com.java.scm.bean.StockRecord;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Mapper
public interface StockRecordDao  extends TkMapper<StockRecord> {
}
