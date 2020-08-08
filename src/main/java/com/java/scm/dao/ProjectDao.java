package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.Project;
import com.java.scm.bean.so.ProjectSO;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工程
 *
 * @author yupan
 * @date 2020-06-23 22:08
 */
@Mapper
public interface ProjectDao extends TkMapper<Project> {

    /**
     * 工程列表
     * @return
     */
    Page<Project> listProject(ProjectSO projectSO);

}
