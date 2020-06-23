package com.java.scm.service.impl;

import com.java.scm.bean.Project;
import com.java.scm.dao.ProjectDao;
import com.java.scm.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Project getProject(Long id) {
        return projectDao.selectByPrimaryKey(id);
    }
}
