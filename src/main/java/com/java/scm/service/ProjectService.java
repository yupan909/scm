package com.java.scm.service;

import com.java.scm.bean.Project;
import com.java.scm.bean.base.BaseResult;

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
    BaseResult getProject(Long id);

    /**
     * 保存工程
     * @param project
     * @return
     */
    BaseResult saveProject(Project project);

    /**
     * 修改工程
     * @param project
     * @return
     */
    BaseResult modifyProject(Project project);

    /**
     * 删除工程
     * @param id
     * @return
     */
    BaseResult deleteProject( Long id);

    /**
     * 展示工程列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    BaseResult listProject(String name,int pageNum,int pageSize);

    /**
     * 停用启用工程
     * @param id
     * @return
     */
    BaseResult stopUsing( Long id);

    /**
     * 根据工程名称查询存在的工程名称
     * @return
     */
    List<String> getProjectByName(List<String> nameList);

}
