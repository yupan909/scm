package com.java.scm.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 出入库
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inout_stock")
public class InoutStock {

    /**
     *
     */
    @Id
    private Long id;

    private Integer warehouseId;

    private String project;

    private String product;

    private String model;

    private String unit;

    private Integer count;

    private String handle;

    private String source;

    private String remark;

    private Boolean type;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createUser;
}