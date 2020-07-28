package com.java.scm.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * license 授权
 * @author hujunhui
 * @date 2020/7/7
 */
@Slf4j
@Configuration
@Order(-1)
public class LicenseConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LicenseInterceptor());
        //所有路径都被拦截
        registration.addPathPatterns("/**");
        //添加不拦截路径
        registration.excludePathPatterns(
                "/license/init",
                "/license/date",
                "/css/**",
                "/html/**",
                "/image/**",
                "/js/**",
                "/**/*.html"

        );
    }
}
