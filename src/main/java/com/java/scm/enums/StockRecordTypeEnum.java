package com.java.scm.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库存变更类型类型枚举
 *
 * @author yupan
 * @date 2020-06-24 11:53
 */
public enum StockRecordTypeEnum {

    入库((byte) 0),
    出库((byte) 1),
    手动修改((byte) 2);

    private byte type;

    StockRecordTypeEnum(byte type) {
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
        StockRecordTypeEnum enums = null;
        if (value != null) {
            enums = cache.get(value);
        }
        return enums == null ? null : enums.name();
    }

    private static Map<Byte, StockRecordTypeEnum> cache = EnumSet.allOf(StockRecordTypeEnum.class).stream()
            .collect(Collectors.toMap(p -> p.type, p -> p));
}
