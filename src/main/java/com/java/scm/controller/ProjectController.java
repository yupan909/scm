package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Project;
import com.java.scm.bean.base.BaseResult;
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
}
