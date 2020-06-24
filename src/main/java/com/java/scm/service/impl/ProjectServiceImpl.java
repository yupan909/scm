package com.java.scm.service.impl;

import com.github.pagehelper.PageHelper;
import com.java.scm.bean.Project;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.dao.ProjectDao;
import com.java.scm.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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


    @Override
    public BaseResult getProject(Long id) {
        Project project = projectDao.selectByPrimaryKey(id);
        return new BaseResult(project);
    }

    @Override
    public BaseResult saveProject(Project project) {
        projectDao.insert(project);
        return new BaseResult(true,"新增成功");
    }

    @Override
    public BaseResult modifyProject(Project project) {
        projectDao.updateByPrimaryKeySelective(project);
        return new BaseResult(true,"修改成功");
    }

    @Override
    public BaseResult deleteProject(Long id) {
        projectDao.deleteByPrimaryKey(id);
        return new BaseResult(true,"删除成功");
    }

    @Override
    public BaseResult listProject(String name, int pageNum, int pageSize) {
        Example example = new Example(Project.class);
        example.setOrderByClause(" create_time DESC ");
        Example.Criteria criteria =  example.createCriteria();
        criteria.andLike("name","%"+name+"%");
        int totalCount = projectDao.selectCountByExample(example);
        PageHelper.startPage(pageNum,pageSize);
        List<Project> projects = projectDao.selectByExample(example);
        return new BaseResult(projects,totalCount);
    }
}
