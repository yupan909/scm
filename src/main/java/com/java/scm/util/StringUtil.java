package com.java.scm.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hujunhui
 * @date 2020/6/24
 */
@Slf4j
public class StringUtil {

    /**
     * 字符串不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return str !=null && !str.trim().isEmpty();
    }

    /**
     * 字符串为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return !isNotEmpty(str);
    }
}
