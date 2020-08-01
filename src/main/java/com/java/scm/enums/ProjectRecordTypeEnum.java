package com.java.scm.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流水帐类型枚举
 *
 * @author yupan
 * @date 2020-06-24 11:53
 */
public enum ProjectRecordTypeEnum {

    收入((byte) 0),
    支出((byte) 1);

    private byte type;

    ProjectRecordTypeEnum(byte type) {
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
        ProjectRecordTypeEnum enums = null;
        if (value != null) {
            enums = cache.get(value);
        }
        return enums == null ? null : enums.name();
    }

    private static Map<Byte, ProjectRecordTypeEnum> cache = EnumSet.allOf(ProjectRecordTypeEnum.class).stream()
            .collect(Collectors.toMap(p -> p.type, p -> p));
}
