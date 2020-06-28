package com.java.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author hujunhui
 * 启动类
 */

@MapperScan(basePackages = {"com.java.scm.dao"})
@SpringBootApplication
@EnableTransactionManagement
public class ScmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScmApplication.class, args);
    }

}
