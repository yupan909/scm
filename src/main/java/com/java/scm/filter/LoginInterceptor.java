package com.java.scm.filter;

import com.java.scm.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            response.sendRedirect(request.getContextPath()+"/login.html");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        return false;
    }
}
