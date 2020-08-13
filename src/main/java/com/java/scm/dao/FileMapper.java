package com.java.scm.dao;

import com.github.pagehelper.Page;
import com.java.scm.bean.File;
import com.java.scm.bean.so.FileSO;
import com.java.scm.tk.TkMapper;

/**
 * 附件
 *
 * @author yupan
 * @date 2020-06-24 12:04
 */
public interface FileMapper extends TkMapper<File> {

    /**
     * 附件列表
     * @return
     */
    Page<File> listFile(FileSO fileSO);

}
