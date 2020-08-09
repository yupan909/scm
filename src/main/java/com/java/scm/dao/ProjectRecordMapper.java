package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.ProjectRecord;
import com.java.scm.bean.so.ProjectRecordSO;
import com.java.scm.tk.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hujunhui
 * @date 2020/6/28
 */
@Mapper
public interface ProjectRecordMapper extends TkMapper<ProjectRecord> {

    /**
     * 工程明细列表
     * @return
     */
    Page<ProjectRecord> listProjectRecord(ProjectRecordSO projectRecordSO);

    /**
     * 获取工程明细金额
     * @return
     */
    List<ProjectRecord> getProjectRecordMoney(@Param("projectIds") List<String> projectIds);
}
