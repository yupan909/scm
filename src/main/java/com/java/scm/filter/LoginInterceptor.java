package com.java.scm.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录过滤器
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
public class LoginInterceptor  implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        log.info("进行登录拦截:"+request.getRequestURI());

        try {
            //统一拦截（查询当前session是否存在user）(这里user会在每次登陆成功后，写入session)
            User user=(User)request.getSession().getAttribute("user");
            if(user!=null){
                return true;
            }
            response.setHeader("sessionstatus","timeout");
            BaseResult result = new BaseResult(false,"用户未登录");
            responseOutWithJson(response,result);
            //response.sendRedirect(request.getContextPath()+"/login.html");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        return false;
    }

    /**
     * 以JSON格式输出
     * @param response
     */
    protected void responseOutWithJson(HttpServletResponse response,
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
