package com.java.scm.config;

/**
 * 返回数据基类
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */
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

    public BaseResult() {
    }

    public BaseResult(T data) {
        this.data = data;
        this.flag = true;
    }

    public BaseResult(Boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
