package com.java.scm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Project;
import com.java.scm.bean.User;
import com.java.scm.bean.so.ProjectSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.ProjectDao;
import com.java.scm.enums.StateEnum;
import com.java.scm.service.ProjectService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

/**
 * 工程
 *
 * @author yupan
 * @date 2020-06-23 22:09
 */
@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    /**
     * 获取工程
     * @param id
     * @return
     */
    @Override
    public Project getProject(String id) {
        AssertUtils.notNull(id, "工程id不能为空");
        Project project = projectDao.selectByPrimaryKey(id);
        return project;
    }

    /**
     * 新增工程
     * @param project
     * @return
     */
    @Override
    public void saveProject(Project project) {
        AssertUtils.notNull(project, "工程信息不能为空");
        AssertUtils.notEmpty(project.getName(), "工程名称不能为空");
        if(!projectCheck(null, project.getName())){
            throw new BusinessException("工程名已存在！");
        }
        User user = RequestUtil.getCurrentUser();
        project.setCreateUserId(user.getId());
        projectDao.insertSelective(project);
    }

    /**
     * 修改工程
     * @param project
     * @return
     */
    @Override
    public void modifyProject(Project project) {
        AssertUtils.notNull(project, "工程信息不能为空");
        AssertUtils.notNull(project.getId(), "工程id不能为空");
        AssertUtils.notEmpty(project.getName(), "工程名称不能为空");
        if(!projectCheck(project.getId(), project.getName())){
            throw new BusinessException("工程名已存在！");
        }
        User user = RequestUtil.getCurrentUser();
        project.setUpdateUserId(user.getId());
        projectDao.updateByPrimaryKeySelective(project);
    }

    /**
     * 删除工程
     * @param id
     * @return
     */
    @Override
    public void deleteProject(String id) {
        AssertUtils.notNull(id, "工程id不能为空");
        projectDao.deleteByPrimaryKey(id);
    }

    /**
     * 工程列表
     * @return
     */
    @Override
    public PageInfo<Project> listProject(ProjectSO projectSO) {
        Page<Project> projectPage = projectDao.listProject(projectSO);
        PageInfo<Project> pageInfo = projectPage.toPageInfo();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            pageInfo.getList().forEach(p -> {
                p.setStateInfo(StateEnum.getEnumByValue(p.getState()));
            });
        }
        return pageInfo;
    }

    /**
     * 停用启用工程
     * @param id
     * @return
     */
    @Override
    public void stopUsing(String id) {
        AssertUtils.notNull(id, "工程id不能为空");
        Project project = projectDao.selectByPrimaryKey(id);
        if (project == null) {
            throw new BusinessException("工程不存在！");
        }
        if(Objects.equals(project.getState(), StateEnum.禁用.getType())){
            project.setState(StateEnum.启用.getType());
        }else{
            project.setState(StateEnum.禁用.getType());
        }
        User user = RequestUtil.getCurrentUser();
        project.setUpdateUserId(user.getId());
        projectDao.updateByPrimaryKeySelective(project);
    }

    /**
     * 根据工程名称查询存在的工程名称
     * @return
     */
    @Override
    public List<Project> getProjectByName(List<String> nameList) {
        AssertUtils.notEmpty(nameList, "工程名称不能为空");
        Example example = new Example(Project.class);
        Example.Criteria criteria =  example.createCriteria();
        criteria.andIn("name", nameList);
        criteria.andEqualTo("state", StateEnum.启用.getType());
        List<Project> projects = projectDao.selectByExample(example);
        return projects;
    }

    /**
     * 检验工程名重复
     * @param id
     * @param name
     * @return
     */
    private boolean projectCheck(String id,String name){
        Example example = new Example(Project.class);
        Example.Criteria criteria =  example.createCriteria();
        if(id !=null){
            criteria.andNotEqualTo("id",id);
        }
        criteria.andEqualTo("name",name);
        int count = projectDao.selectCountByExample(example);
        if(count >0){
            return false;
        }else{
            return true;
        }
    }
}
