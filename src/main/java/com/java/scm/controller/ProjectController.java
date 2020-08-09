package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Project;
import com.java.scm.bean.ProjectRecord;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.so.ProjectRecordSO;
import com.java.scm.bean.so.ProjectSO;
import com.java.scm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工程
 *
 * @author yupan
 * @date 2020-06-23 22:35
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 查询工程详情
     * @return
     */
    @GetMapping("/getProject/{id}")
    public BaseResult getProject(@PathVariable("id") String id) {
        Project project = projectService.getProject(id);
        return new BaseResult(project);
    }

    /**
     * 保存工程
     * @return
     */
    @PostMapping("/save")
    public BaseResult saveProject(@RequestBody Project project){
        projectService.saveProject(project);
        return BaseResult.successResult();
    }

    /**
     * 修改工程
     * @return
     */
    @PostMapping("/modify")
    public BaseResult modifyProject(@RequestBody Project project){
        projectService.modifyProject(project);
        return BaseResult.successResult();
    }

    /**
     * 删除工程
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResult deleteProject(@PathVariable("id") String id){
        projectService.deleteProject(id);
        return BaseResult.successResult();
    }

    /**
     * 查询工程列表
     * @return
     */
    @PostMapping("/list")
    public BaseResult listProject(@RequestBody ProjectSO projectSO){
        PageInfo<Project> pageInfo = projectService.listProject(projectSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 停用启用工程
     * @return
     */
    @GetMapping("/stopUsing/{id}")
    public BaseResult stopUsing(@PathVariable("id") String id){
        projectService.stopUsing(id);
        return BaseResult.successResult();
    }

    /**
     * 查询工程明细列表
     * @return
     */
    @PostMapping("/detail")
    public BaseResult listProjectRecord(@RequestBody ProjectRecordSO projectRecordSO){
        PageInfo<ProjectRecord> pageInfo = projectService.listProjectRecord(projectRecordSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 获取工程明细详情
     * @return
     */
    @GetMapping("/getDetail/{id}")
    public BaseResult getProjectRecord(@PathVariable("id") String id){
        ProjectRecord projectRecord = projectService.getProjectRecord(id);
        return new BaseResult(projectRecord);
    }

    /**
     * 保存工程明细
     * @return
     */
    @PostMapping("/addDetail")
    public BaseResult saveProjectRecord(@RequestBody ProjectRecord projectRecord){
        projectService.saveProjectRecord(projectRecord);
        return BaseResult.successResult();
    }

    /**
     * 修改工程明细
     * @return
     */
    @PostMapping("/modifyDetail")
    public BaseResult updateProjectRecord(@RequestBody ProjectRecord projectRecord){
        projectService.updateProjectRecord(projectRecord);
        return BaseResult.successResult();
    }
}
