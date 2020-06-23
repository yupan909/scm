package com.java.scm.tk;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * TkMapper
 *
 * @author yupan
 * @date 2020-06-23 21:38
 */
@Component
public interface TkMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
