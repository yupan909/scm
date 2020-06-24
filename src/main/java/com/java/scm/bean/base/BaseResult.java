package com.java.scm.bean.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回数据基类
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */
@Getter
@Setter
public class BaseResult<T> {

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回标识（true: 成功）
     */
    private Boolean flag;

    /**
     * 错误消息
     */
    private String message;

    private int totalCount;

    public BaseResult() {
    }

    public BaseResult(T data) {
        this.data = data;
        this.flag = true;
    }

    public BaseResult(T data,int totalCount) {
        this.data = data;
        this.totalCount = totalCount;
        this.flag = true;
    }

    public BaseResult(Boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }
    /**
     * 成功返回
     *
     * @return
     */
    public static BaseResult successResult() {
        return new BaseResult(true, "");
    }

    /**
     * 错误返回
     *
     * @param message
     * @return
     */
    public static BaseResult errorResult(String message) {
        return new BaseResult(false, message);
    }
}
