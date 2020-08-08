package com.java.scm.bean.so;

import com.java.scm.bean.base.PageCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户查询
 *
 * @author yupan
 * @date 2020-08-08 21:37
 */
@Getter
@Setter
public class UserSO extends PageCondition {

    /**
     * 关键字，根据姓名，手机号
     */
    private String key;
}
