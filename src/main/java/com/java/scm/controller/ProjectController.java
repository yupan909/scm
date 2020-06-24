package com.java.scm.controller;

import com.java.scm.bean.Project;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Project project = projectService.getProject(id);
        return new BaseResult(project);
    }
}
