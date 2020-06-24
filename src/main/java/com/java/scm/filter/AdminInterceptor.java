package com.java.scm.filter;

import com.alibaba.fastjson.JSON;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 超管过滤器
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private static final Byte IS_ADMIN = (byte)1;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("进行超管权限拦截拦截:"+ request.getRequestURI());
        try {
            User user=(User)request.getSession().getAttribute("user");
            if(user!=null){
                Byte isAdmin =  user.getAdmin();
                if(IS_ADMIN.equals(isAdmin)){
                    return true;
                }else{
                    responseResult(response, BaseResult.errorResult("您无权执行此操作"));
                    return false;
                }

            }
            response.sendRedirect(request.getContextPath()+"/login.html");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        return false;
    }

    private void responseResult(HttpServletResponse response, Object result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer=null;
        try {
            writer=response.getWriter();
            writer.write(JSON.toJSONString(result));
            writer.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }finally {
            if(writer!=null) {
                writer.close();
            }
        }
    }
}
