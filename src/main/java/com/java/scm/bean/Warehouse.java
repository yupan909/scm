package com.java.scm.bean;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 仓库表
 *
 * @author yupan
 * @date 2020/6/23 10:44
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse")
public class Warehouse {

    /**
     * 仓库id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 仓库名称
     */
    private String name;

}