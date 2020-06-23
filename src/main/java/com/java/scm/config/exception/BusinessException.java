package com.java.scm.config.exception;

/**
 * 业务异常
 *
 * @author yupan@yijiupi.cn
 * @date 2018/9/4 16:09
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
