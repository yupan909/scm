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

    @GetMapping("/getProject/{id}")
    public BaseResult getProject(@PathVariable("id") Long id) {
        return  projectService.getProject(id);
    }

    @PostMapping("/save")
    public BaseResult saveProject(@RequestBody Project project){
        return projectService.saveProject(project);
    }

    @PostMapping("/modify")
    public BaseResult modifyProject(@RequestBody Project project){
        return projectService.modifyProject(project);
    }
    @GetMapping("/delete/{id}")
    public BaseResult deleteProject(@PathVariable("id") Long id){
        return projectService.deleteProject(id);
    }

    @GetMapping("/list")
    public BaseResult listProject(String name,int pageNum,int pageSize){
        return projectService.listProject(name,pageNum,pageSize);
    }
    @GetMapping("/stopUsing/{id}")
    public BaseResult stopUsing(@PathVariable("id") Long id){
        return projectService.stopUsing(id);
    }
}
