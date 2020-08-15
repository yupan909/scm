package com.java.scm.controller;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.File;
import com.java.scm.bean.Project;
import com.java.scm.bean.ProjectRecord;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.bean.excel.ProjectExportTemplate;
import com.java.scm.bean.excel.ProjectRecordExportTemplate;
import com.java.scm.bean.so.FileSO;
import com.java.scm.bean.so.ProjectRecordSO;
import com.java.scm.bean.so.ProjectSO;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.service.FileService;
import com.java.scm.service.ProjectService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.DateUtils;
import com.java.scm.util.FileUtils;
import com.java.scm.util.StringUtil;
import com.java.scm.util.excel.ExcelUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private FileService fileService;

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
     * 导出工程
     * @throws Exception
     */
    @GetMapping("/exportProject")
    public void exportProject(@RequestParam("name") String name,
                            HttpServletResponse response) throws Exception {
        // 查询数据
        ProjectSO projectSO = new ProjectSO();
        if (StringUtil.isNotEmpty(name)) {
            projectSO.setName(URLDecoder.decode(name, "utf-8"));
        }
        PageInfo<Project> pageInfo = projectService.listProject(projectSO);
        List<ProjectExportTemplate> exportList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pageInfo.getList())) {
            for(int i = 0; i < pageInfo.getList().size(); i++) {
                Project project = pageInfo.getList().get(i);
                ProjectExportTemplate template = new ProjectExportTemplate();
                BeanUtils.copyProperties(project, template);
                template.setNum(String.valueOf(i+1));
                template.setContractMoney(project.getContractMoney() != null ? project.getContractMoney().stripTrailingZeros().toPlainString() : "");
                template.setFinalMoney(project.getFinalMoney() != null ? project.getFinalMoney().stripTrailingZeros().toPlainString() : "");
                template.setInMoney(project.getInMoney() != null ? project.getInMoney().stripTrailingZeros().toPlainString() : "");
                template.setOutMoney(project.getOutMoney() != null ? project.getOutMoney().stripTrailingZeros().toPlainString() : "");
                template.setSumMoney(project.getSumMoney() != null ? project.getSumMoney().stripTrailingZeros().toPlainString() : "");
                exportList.add(template);
            }
        }
        ExcelUtils.exportExcel(exportList, ProjectExportTemplate.class, "工程管理", "工程管理", response);
    }

    /**
     * 导出工程明细
     * @throws Exception
     */
    @GetMapping("/exportProjectDetail")
    public void exportProjectDetail(@RequestParam("projectId") String projectId,
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
                exportList.add(template);
            }
        }
        ExcelUtils.exportExcel(exportList, ProjectRecordExportTemplate.class, "工程流水账", "工程流水账", response);
    }

    /**
     * 获取附件列表
     * @return
     */
    @PostMapping("/listFile")
    public BaseResult listFile(@RequestBody FileSO fileSO){
        PageInfo<File> pageInfo = fileService.listFile(fileSO);
        return new BaseResult(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 删除附件
     * @return
     */
    @GetMapping("/deleteFile/{id}")
    public BaseResult deleteFile(@PathVariable("id") String id){
        AssertUtils.notEmpty(id, "附件id不能为空！");
        fileService.deleteFile(id);
        return BaseResult.successResult();
    }

    /**
     * 上传附件
     */
    @PostMapping("/upload")
    public BaseResult upload(@RequestParam("file") MultipartFile file, @RequestParam("businessId") String businessId) throws Exception {
        AssertUtils.notNull(file, "上传文件不能为空！");
        AssertUtils.notEmpty(businessId, "业务id不能为空！");
        // 上传服务器
        String fileUrl = FileUtils.upload(file);
        // 保存文件表
        File filePO = new File();
        filePO.setName(file.getOriginalFilename());
        filePO.setUrl(fileUrl);
        filePO.setBusinessId(businessId);
        fileService.saveFile(filePO);
        return BaseResult.successResult();
    }

    /**
     * 下载附件
     */
    @GetMapping("/download/{id}")
    public BaseResult downloadFile(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        AssertUtils.notEmpty(id, "附件id不能为空！");
        File file = fileService.getFile(id);
        if (file == null) {
            throw new BusinessException("找不到附件！");
        }
        FileUtils.download(file.getName(), file.getUrl(), response);
        return null;
    }
}
