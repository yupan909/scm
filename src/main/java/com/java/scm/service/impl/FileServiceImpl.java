package com.java.scm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.java.scm.bean.File;
import com.java.scm.bean.so.FileSO;
import com.java.scm.dao.FileMapper;
import com.java.scm.service.FileService;
import com.java.scm.util.AssertUtils;
import com.java.scm.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * 附件
 *
 * @author yupan
 * @date 2020-06-24 12:05
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;
    /**
     * 附件列表
     * @return
     */
    @Override
    public PageInfo<File> listFile(FileSO fileSO) {
        AssertUtils.notNull(fileSO, "附件列表参数不能为空！");
        AssertUtils.notEmpty(fileSO.getBusinessId(), "业务id不能为空！");
        Page<File> page = fileMapper.listFile(fileSO);
        return page.toPageInfo();
    }

    /**
     * 获取附件详情
     */
    @Override
    public File getFile(String id) {
        AssertUtils.notEmpty(id, "附件id不能为空！");
        return fileMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存附件
     */
    @Override
    public void saveFile(File file) {
        AssertUtils.notNull(file, "附件参数不能为空！");
        AssertUtils.notEmpty(file.getName(), "附件名不能为空！");
        AssertUtils.notEmpty(file.getUrl(), "存储路径不能为空！");
        AssertUtils.notEmpty(file.getBusinessId(), "业务id不能为空！");
        file.setCreateUserId(RequestUtil.getCurrentUser().getId());
        fileMapper.insertSelective(file);
    }

    /**
     * 删除附件
     */
    @Override
    public void deleteFile(String id) {
        AssertUtils.notEmpty(id, "附件id不能为空！");
        fileMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据业务id删除附件
     */
    @Override
    public void deleteFileByBusinessId(String businessId) {
        AssertUtils.notEmpty(businessId, "业务id不能为空！");
        Example example = new Example(File.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessId", businessId);
        fileMapper.deleteByExample(example);
    }
}
