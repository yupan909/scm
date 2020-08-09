package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.Project;
import com.java.scm.bean.ProjectRecord;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.excel.ProjectRecordExportTemplate;
import com.java.scm.bean.so.ProjectRecordSO;
import com.java.scm.bean.so.ProjectSO;
import com.java.scm.service.ProjectService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.DateUtils;
import com.java.scm.util.StringUtil;
import com.java.scm.util.excel.ExcelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 删除工程明细
     * @return
     */
    @GetMapping("/deleteDetail/{id}")
    public BaseResult deleteProjectRecord(@PathVariable("id") String id){
        projectService.deleteProjectRecord(id);
        return BaseResult.successResult();
    }

    /**
     * 导出工程明细
     * @throws Exception
     */
    @GetMapping("/exportProjectDetail")
    public void exportStock(@RequestParam("projectId") String projectId,
                            @RequestParam("type") Byte type,
                            @RequestParam("digest") String digest,
                            HttpServletResponse response) throws Exception {
        AssertUtils.notNull(projectId, "工程id不能为空");
        // 查询数据
        ProjectRecordSO projectRecordSO = new ProjectRecordSO();
        projectRecordSO.setProjectId(projectId);
        projectRecordSO.setType(type);
        if (StringUtil.isNotEmpty(digest)) {
            projectRecordSO.setDigest(URLDecoder.decode(digest, "utf-8"));
        }
        PageInfo<ProjectRecord> pageInfo = projectService.listProjectRecord(projectRecordSO);

        List<ProjectRecordExportTemplate> exportList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            for(int i = 0; i < pageInfo.getList().size(); i++) {
                ProjectRecord projectRecord = pageInfo.getList().get(i);
                ProjectRecordExportTemplate template = new ProjectRecordExportTemplate();
                BeanUtils.copyProperties(projectRecord, template);
                template.setNum(String.valueOf(i+1));
                template.setType(projectRecord.getTypeInfo());
                template.setMoney(projectRecord.getMoney() != null ? projectRecord.getMoney().stripTrailingZeros().toPlainString() : "");
                template.setCreateTime(DateUtils.formatDateTime(projectRecord.getCreateTime()));
                exportList.add(template);
            }
        }
        ExcelUtils.exportExcel(exportList, ProjectRecordExportTemplate.class, "工程流水账", "工程流水账", response);
    }
}
