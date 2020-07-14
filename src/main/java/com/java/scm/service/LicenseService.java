package com.java.scm.service;

import com.java.scm.bean.base.BaseResult;

/**
 * @author hujunhui
 * @date 2020/7/8
 */
public interface LicenseService {

    /**
     * 初始化license
     * @param license
     * @return
     */
    BaseResult init(String license);

    /**
     * 获取license的可用时间
     * @return
     */
    BaseResult getLicenseDate();
}
