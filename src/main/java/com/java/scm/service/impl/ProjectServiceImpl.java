package com.java.scm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Project;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.ProjectDao;
import com.java.scm.enums.StateEnum;
import com.java.scm.service.ProjectService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
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
    public BaseResult getProject(Long id) {
        AssertUtils.notNull(id, "工程id不能为空");
        Project project = projectDao.selectByPrimaryKey(id);
        return new BaseResult(project);
    }

    /**
     * 新增工程
     * @param project
     * @return
     */
    @Override
    public BaseResult saveProject(Project project) {
        AssertUtils.notNull(project, "工程信息不能为空");
        AssertUtils.notEmpty(project.getName(), "工程名称不能为空");
        if(!projectCheck(null,project.getName())){
            return new BaseResult(false,"工程名不可重复");
        }
        projectDao.insertSelective(project);
        return new BaseResult(true,"新增成功");
    }

    /**
     * 修改工程
     * @param project
     * @return
     */
    @Override
    public BaseResult modifyProject(Project project) {
        AssertUtils.notNull(project, "工程信息不能为空");
        AssertUtils.notNull(project.getId(), "工程id不能为空");
        if(!projectCheck(project.getId(),project.getName())){
            return new BaseResult(false,"工程名不可重复");
        }
        projectDao.updateByPrimaryKeySelective(project);
        return new BaseResult(true,"修改成功");
    }

    /**
     * 删除工程
     * @param id
     * @return
     */
    @Override
    public BaseResult deleteProject(Long id) {
        AssertUtils.notNull(id, "工程id不能为空");
        projectDao.deleteByPrimaryKey(id);
        return new BaseResult(true,"删除成功");
    }

    /**
     * 工程列表
     * @return
     */
    @Override
    public BaseResult listProject(String name, int pageNum, int pageSize) {
        Example example = new Example(Project.class);
        example.setOrderByClause(" create_time DESC ");
        Example.Criteria criteria =  example.createCriteria();
        criteria.andLike("name","%"+name+"%");
        PageHelper.startPage(pageNum,pageSize);
        List<Project> projects = projectDao.selectByExample(example);
        PageInfo<Project> pageInfo = new PageInfo<>(projects);
        for (Project one : projects){
            one.setStateInfo( StateEnum.getEnumByValue(one.getState()));
        }
        return new BaseResult(projects,pageInfo.getTotal());
    }

    /**
     * 停用启用工程
     * @param id
     * @return
     */
    @Override
    public BaseResult stopUsing(Long id) {
        AssertUtils.notNull(id, "工程id不能为空");
        Project project = projectDao.selectByPrimaryKey(id);
        if (project == null) {
            throw new BusinessException("工程不存在！");
        }
        String msg;
        if(Objects.equals(project.getState(), StateEnum.禁用.getType())){
            project.setState(StateEnum.启用.getType());
            msg = "工程已启用";
        }else{
            project.setState(StateEnum.禁用.getType());
            msg = "工程已停用";
        }
        project.setUpdateTime(new Date());
        projectDao.updateByPrimaryKeySelective(project);
        return new BaseResult(true,msg);
    }

    /**
     * 检验工程名重复
     * @param id
     * @param name
     * @return
     */
    private boolean projectCheck(Long id,String name){
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
