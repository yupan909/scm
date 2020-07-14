package com.java.scm.service.impl;

import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.service.LicenseService;
import com.java.scm.util.LicenseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hujunhui
 * @date 2020/7/8
 */
@Slf4j
@Service
public class LicenseServiceImpl implements LicenseService {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public BaseResult init(String license) {
        Date date = LicenseUtil.initLicense(license);
        return new BaseResult(DATE_FORMAT.format(date));
    }

    @Override
    public BaseResult getLicenseDate() {
        Date date = LicenseUtil.getLicenseDate();
        Date now = new Date();
        if(date.before(now)){
            throw new BusinessException("系统授权至："+DATE_FORMAT.format(date)+"，已过期");
        }
        return new BaseResult(DATE_FORMAT.format(date));
    }
}
