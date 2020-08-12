package com.java.scm.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色枚举
 *
 * @author yupan
 * @date 2020-06-24 11:53
 */
public enum RoleEnum {

    仓库普通人员((byte) 0),
    仓库管理员((byte) 1),
    超级管理员((byte) 2);

    private byte type;

    RoleEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    /**
     * 根据枚举值获取枚举对象名称
     * @param value
     * @return
     */
    public static String getEnumByValue(Byte value) {
        RoleEnum enums = null;
        if (value != null) {
            enums = cache.get(value);
        }
        return enums == null ? null : enums.name();
    }

    private static Map<Byte, RoleEnum> cache = EnumSet.allOf(RoleEnum.class).stream()
            .collect(Collectors.toMap(p -> p.type, p -> p));
}
