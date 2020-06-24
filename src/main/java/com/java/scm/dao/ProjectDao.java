package com.java.scm.dao;

import com.java.scm.bean.Project;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 工程
 *
 * @author yupan
 * @date 2020-06-23 22:08
 */
@Mapper
@Component
public interface ProjectDao extends TkMapper<Project> {

}
