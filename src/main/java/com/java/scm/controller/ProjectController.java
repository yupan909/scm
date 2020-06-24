package com.java.scm.controller;

import com.java.scm.bean.Project;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * todo
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
        return null;
    }

    @PostMapping("/modify")
    public BaseResult modifyProject(@RequestBody Project project){
        return null;
    }
    @GetMapping("/delete/{id}")
    public BaseResult deleteProject(@PathVariable("id") Long id){
        return null;
    }

    @GetMapping("/list")
    public BaseResult listProject(String name,int pageNum,int pageSize){
        return null;
    }
}
