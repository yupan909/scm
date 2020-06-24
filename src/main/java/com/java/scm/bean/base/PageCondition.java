package com.java.scm.bean.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页
 *
 * @author yupan
 * @date 2020-06-24 12:12
 */
@Getter
@Setter
public class PageCondition {

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 分页大小
     */
    private Integer pageSize;

}
