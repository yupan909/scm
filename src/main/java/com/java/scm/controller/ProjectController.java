package com.java.scm.controller;

import com.java.scm.bean.Project;
import com.java.scm.bean.base.BaseResult;
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
        return  projectService.getProject(id);
    }

    /**
     * 保存工程
     * @return
     */
    @PostMapping("/save")
    public BaseResult saveProject(@RequestBody Project project){
        return projectService.saveProject(project);
    }

    /**
     * 修改工程
     * @return
     */
    @PostMapping("/modify")
    public BaseResult modifyProject(@RequestBody Project project){
        return projectService.modifyProject(project);
    }

    /**
     * 删除工程
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResult deleteProject(@PathVariable("id") String id){
        return projectService.deleteProject(id);
    }

    /**
     * 查询工程列表
     * @return
     */
    @GetMapping("/list")
    public BaseResult listProject(String name,int pageNum,int pageSize){
        return projectService.listProject(name,pageNum,pageSize);
    }

    /**
     * 停用启用工程
     * @return
     */
    @GetMapping("/stopUsing/{id}")
    public BaseResult stopUsing(@PathVariable("id") String id){
        return projectService.stopUsing(id);
    }
}
