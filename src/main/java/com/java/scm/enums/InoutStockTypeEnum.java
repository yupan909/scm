package com.java.scm.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出入库单类型枚举
 *
 * @author yupan
 * @date 2020-06-24 11:53
 */
public enum InoutStockTypeEnum {

    入库((byte) 0),
    出库((byte) 1);

    private byte type;

    InoutStockTypeEnum(byte type) {
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
        InoutStockTypeEnum enums = null;
        if (value != null) {
            enums = cache.get(value);
        }
        return enums == null ? null : enums.name();
    }

    private static Map<Byte, InoutStockTypeEnum> cache = EnumSet.allOf(InoutStockTypeEnum.class).stream()
            .collect(Collectors.toMap(p -> p.type, p -> p));
}
