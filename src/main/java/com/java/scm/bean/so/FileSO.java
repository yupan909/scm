package com.java.scm.bean.so;

import com.java.scm.bean.base.PageCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 附件查询
 *
 * @author yupan
 * @date 2020-08-08 21:37
 */
@Getter
@Setter
public class FileSO extends PageCondition {

    /**
     * 业务id
     */
    private String businessId;

}
