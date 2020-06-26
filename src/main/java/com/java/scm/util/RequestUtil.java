package com.java.scm.util;

import com.java.scm.bean.User;
import com.java.scm.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
public class RequestUtil {

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;

    }

    public static HttpSession getSession() {
        HttpSession session = getRequest().getSession();
        return session;
    }

    public static User getLoginUser() {
         User user = (User)getSession().getAttribute("user");
         if (user == null) {
             throw new BusinessException("获取用户信息失败！");
         }
         return user;
    }
}
