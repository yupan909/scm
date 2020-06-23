package com.java.scm.config.exception;

import com.java.scm.config.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author Administrator
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult exceptionHandler(Exception exception) {
        LOGGER.error("异常信息", exception);
        String message = "运行异常，请联系管理员！";
        if (exception instanceof BusinessException) {
            message = exception.getMessage();
        }
        return BaseResult.errorResult(exception.getMessage());
    }
}
