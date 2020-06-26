package com.java.scm.bean.vo;

import com.java.scm.bean.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hujunhui
 * @date 2020/6/26
 */
@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVo extends User {
    private String adminInfo;
    private String stateInfo;
    private String warehouseName;
}
