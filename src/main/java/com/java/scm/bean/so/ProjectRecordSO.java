package com.java.scm.bean.so;

import com.java.scm.bean.base.PageCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * 工程明细查询
 *
 * @author yupan
 * @date 2020-08-08 21:37
 */
@Getter
@Setter
public class ProjectRecordSO extends PageCondition {

    /**
     * 工程id
     */
    private String projectId;

    /**
     * 工程名称
     */
    private String project;

    /**
     * 流水帐类型 0：收入 1：支出
     */
    private Byte type;

    /**
     * 摘要
     */
    private String digest;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
