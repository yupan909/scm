package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.Project;
import com.java.scm.bean.so.ProjectSO;
import com.java.scm.tk.TkMapper;

/**
 * 工程
 *
 * @author yupan
 * @date 2020-06-23 22:08
 */
public interface ProjectMapper extends TkMapper<Project> {

    /**
     * 工程列表
     * @return
     */
    Page<Project> listProject(ProjectSO projectSO);

}
