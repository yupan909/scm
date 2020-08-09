package com.java.scm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Project;
import com.java.scm.bean.ProjectRecord;
import com.java.scm.bean.User;
import com.java.scm.bean.so.ProjectRecordSO;
import com.java.scm.bean.so.ProjectSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.dao.ProjectMapper;
import com.java.scm.dao.ProjectRecordMapper;
import com.java.scm.enums.ProjectRecordTypeEnum;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectRecordMapper projectRecordMapper;

    /**
     * 获取工程
     * @param id
     * @return
     */
    @Override
    public Project getProject(String id) {
        AssertUtils.notNull(id, "工程id不能为空");
        Project project = projectMapper.selectByPrimaryKey(id);
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
        projectMapper.insertSelective(project);
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
        projectMapper.updateByPrimaryKeySelective(project);
    }

    /**
     * 删除工程
     * @param id
     * @return
     */
    @Override
    public void deleteProject(String id) {
        AssertUtils.notNull(id, "工程id不能为空");
        projectMapper.deleteByPrimaryKey(id);
    }

    /**
     * 工程列表
     * @return
     */
    @Override
    public PageInfo<Project> listProject(ProjectSO projectSO) {
        Page<Project> projectPage = projectMapper.listProject(projectSO);
        PageInfo<Project> pageInfo = projectPage.toPageInfo();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            // 获取流水帐统计金额
            List<String> projectIds = pageInfo.getList().stream().map(p -> p.getId()).collect(Collectors.toList());
            List<ProjectRecord> projectRecordList = projectRecordMapper.getProjectRecordMoney(projectIds);

            pageInfo.getList().forEach(p -> {
                p.setStateInfo(StateEnum.getEnumByValue(p.getState()));
                // 收入金额
                Optional<ProjectRecord> optionalProjectRecordIn = projectRecordList.stream().filter(record -> Objects.equals(record.getProjectId(), p.getId()) && Objects.equals(record.getType(), ProjectRecordTypeEnum.收入.getType())).findFirst();
                if (optionalProjectRecordIn.isPresent()) {
                    p.setInMoney(optionalProjectRecordIn.get().getMoney());
                    // 合计金额
                    p.setSumMoney(p.getInMoney());
                }
                // 支出金额
                Optional<ProjectRecord> optionalProjectRecordOut = projectRecordList.stream().filter(record -> Objects.equals(record.getProjectId(), p.getId()) && Objects.equals(record.getType(), ProjectRecordTypeEnum.支出.getType())).findFirst();
                if (optionalProjectRecordOut.isPresent()) {
                    p.setOutMoney(optionalProjectRecordOut.get().getMoney());
                    // 合计金额
                    p.setSumMoney(p.getSumMoney() != null ? p.getSumMoney().add(p.getOutMoney()) : p.getOutMoney());
                }
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
        Project project = projectMapper.selectByPrimaryKey(id);
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
        projectMapper.updateByPrimaryKeySelective(project);
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
        List<Project> projects = projectMapper.selectByExample(example);
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
        int count = projectMapper.selectCountByExample(example);
        if(count >0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 工程明细列表
     * @return
     */
    @Override
    public PageInfo<ProjectRecord> listProjectRecord(ProjectRecordSO projectRecordSO){
        AssertUtils.notNull(projectRecordSO, "工程明细列表参数不能为空");
        AssertUtils.notNull(projectRecordSO.getProjectId(), "工程id不能为空");
        Page<ProjectRecord> projectRecordPage = projectRecordMapper.listProjectRecord(projectRecordSO);
        // 类型
        if (!CollectionUtils.isEmpty(projectRecordPage.toPageInfo().getList())) {
            projectRecordPage.toPageInfo().getList().forEach(p -> {
                p.setTypeInfo(ProjectRecordTypeEnum.getEnumByValue(p.getType()));
            });
        }
        return projectRecordPage.toPageInfo();
    }

    /**
     * 获取工程明细详情
     * @return
     */
    @Override
    public ProjectRecord getProjectRecord(String recordId) {
        AssertUtils.notNull(recordId, "工程明细id不能为空");
        ProjectRecord record = projectRecordMapper.selectByPrimaryKey(recordId);
        return record;
    }

    /**
     * 保存工程明细
     */
    @Override
    public void saveProjectRecord(ProjectRecord projectRecord) {
        AssertUtils.notNull(projectRecord, "工程明细不能为空");
        AssertUtils.notNull(projectRecord.getProjectId(), "工程id不能为空");
        AssertUtils.notNull(projectRecord.getType(), "类型不能为空");
        AssertUtils.notNull(projectRecord.getRecordDate(), "日期不能为空");
        AssertUtils.notNull(projectRecord.getMoney(), "金额不能为空");
        User user = RequestUtil.getCurrentUser();
        projectRecord.setCreateUserId(user.getId());
        projectRecordMapper.insertSelective(projectRecord);
    }

    /**
     * 修改工程明细
     */
    @Override
    public void updateProjectRecord(ProjectRecord projectRecord) {
        AssertUtils.notNull(projectRecord, "工程明细不能为空");
        AssertUtils.notNull(projectRecord.getId(), "工程明细id不能为空");
        User user = RequestUtil.getCurrentUser();
        projectRecord.setUpdateUserId(user.getId());
        projectRecordMapper.updateByPrimaryKeySelective(projectRecord);
    }

    /**
     * 删除工程明细
     */
    @Override
    public void deleteProjectRecord(String recordId) {
        AssertUtils.notNull(recordId, "工程明细id不能为空");
        projectRecordMapper.deleteByPrimaryKey(recordId);
    }

}
