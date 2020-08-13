package com.java.scm.service;

import com.github.pagehelper.PageInfo;
import com.java.scm.bean.File;
import com.java.scm.bean.so.FileSO;

/**
 * 附件
 *
 * @author yupan
 * @date 2020-06-24 12:05
 */
public interface FileService {

    /**
     * 附件列表
     * @return
     */
    PageInfo<File> listFile(FileSO fileSO);

    /**
     * 获取附件详情
     */
    File getFile(String id);

    /**
     * 保存附件
     */
    void saveFile(File file);

    /**
     * 删除附件
     */
    void deleteFile(String id);

    /**
     * 根据业务id删除附件
     */
    void deleteFileByBusinessId(String businessId);

}
