package com.java.scm.util;

import com.github.pagehelper.PageHelper;

/**
 * 分页工具类
 *
 * @author yupan
 * @date 2020-06-25 18:16
 */
public class PageUtils {

    /**
     * 添加分页
     * @param pageNum
     * @param pageSize
     */
    public static void addPage(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
    }
}
