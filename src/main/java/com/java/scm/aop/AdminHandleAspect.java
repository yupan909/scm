package com.java.scm.aop;

import com.java.scm.bean.User;
import com.java.scm.bean.base.BaseResult;
import com.java.scm.config.exception.BusinessException;
import com.java.scm.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Aspect
@Component
@Slf4j
public class AdminHandleAspect {

    private static final Byte IS_ADMIN = (byte)1;

    @Pointcut("@annotation(com.java.scm.aop.anno.AdminRight)")
    public void serviceAspect() {
        // 以注解作为切点
    }

    @Before("serviceAspect()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException {
        HttpServletRequest request = RequestUtil.getRequest();
        log.info("切面超管过滤 :"+request.getRequestURI());
        User user= RequestUtil.getCurrentUser();
        if(user == null ){
            throw new BusinessException("用户未登录");
        }else if(!IS_ADMIN.equals(user.getAdmin())){
            throw new BusinessException("您无此功能权限");
        }
    }


}
