package com.java.scm.filter;

import com.alibaba.fastjson.JSON;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.util.FileUtil;
import com.java.scm.util.LicenseUtil;
import com.java.scm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.Line;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author hujunhui
 * @date 2020/7/7
 */
@Slf4j
public class LicenseInterceptor implements HandlerInterceptor {

    private static String LICENSE_NAME = "scm.lic";

    private static String DIR = ".config";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("license过滤");
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        String path = jarF.getParentFile().toString();
        path = path + File.separator + DIR + File.separator + LICENSE_NAME;
        String licenseInfo ;
        try {
             licenseInfo = FileUtil.read(path);
             if(StringUtil.isEmpty(licenseInfo)){
                 throw new BusinessException("license 为空");
             }
        }catch (BusinessException e){
            BaseResult result = new BaseResult(false,"系统未授权，请联系系统管理员");
            responseOutWithJson(response,result);
            return false;
        }
        Date dDate ;
        try {
            dDate = LicenseUtil.getLicenseDate(licenseInfo);
        }catch ( BusinessException e){
            BaseResult result = new BaseResult(false,"系统license无效，请联系系统管理员");
            responseOutWithJson(response,result);
            return false;
        }
        Date now = new Date();
        if(dDate.before(now)){
            BaseResult result = new BaseResult(false,"系统license已过期，请联系系统管理员");
            responseOutWithJson(response,result);
            return false;
        }
        return true;
    }

    /**
     * 以JSON格式输出
     * @param response
     */
    private  void responseOutWithJson(HttpServletResponse response,
                                       Object responseObject) {
        //将实体对象转换为JSON Object转换
        String re = JSON.toJSONString(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out  = response.getWriter()){
            out.append(re);
            out.flush();
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
    }
}
