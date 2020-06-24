package com.java.scm.util;

import com.java.scm.bean.User;
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

    private static final String USER = "user";

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;

    }

    public static HttpSession getSession() {
        HttpSession session = getRequest().getSession();
        return session;
    }

    public static User getCurrentUser(){
        return  (User)getSession().getAttribute(USER);
    }

    public static void setLoginUser(User user){
        getSession().setAttribute(USER,user);
    }

    public static void removeLoginUser(){
        getSession().removeAttribute(USER);
    }
}
