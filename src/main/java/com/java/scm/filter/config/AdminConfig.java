package com.java.scm.filter.config;

import com.java.scm.filter.AdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
@Configuration
@Order(1)
public class AdminConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new AdminInterceptor());
        //所有路径都被拦截
        registration.addPathPatterns("/user/**");
        //添加不拦截路径
        registration.excludePathPatterns(
                "/user/login",
                "/user/logout"
        );
    }
}
