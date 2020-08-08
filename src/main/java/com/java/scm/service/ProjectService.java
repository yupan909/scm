package com.java.scm.service;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Project;
import com.java.scm.bean.so.ProjectSO;

import java.util.List;

/**
 * 工程
 *
 * @author yupan
 * @date 2020-06-23 22:09
 */
public interface ProjectService {

    /**
     * 获取工程
     * @param id
     * @return
     */
    Project getProject(String id);

    /**
     * 保存工程
     * @param project
     * @return
     */
    void saveProject(Project project);

    /**
     * 修改工程
     * @param project
     * @return
     */
    void modifyProject(Project project);

    /**
     * 删除工程
     * @param id
     * @return
     */
    void deleteProject(String id);

    /**
     * 展示工程列表
     * @return
     */
    PageInfo<Project> listProject(ProjectSO projectSO);

    /**
     * 停用启用工程
     * @param id
     * @return
     */
    void stopUsing(String id);

    /**
     * 根据工程名称查询工程
     * @return
     */
    List<Project> getProjectByName(List<String> nameList);

}
