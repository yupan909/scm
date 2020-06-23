package com.java.scm.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author yupan@yijiupi.cn
 * @date 2019-11-15 16:03
 */
public class DateUtils {

    /**
     * 日期格式化 yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Date转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转Date
     *
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDate转Date
     *
     * @return
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalTime转Date
     *
     * @return
     */
    public static Date toDate(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return toDate(localDateTime);
    }

    /**
     * 年月日时分秒格式的日期转化为字符串
     *
     * @return
     */
    public static String formatDateTime(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(formatter);
    }

    /**
     * 年月日时分秒格式的日期转化为字符串
     *
     * @return
     */
    public static String formatDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DATETIME);
    }

    /**
     * 年月日格式的日期转化为字符串
     *
     * @return
     */
    public static String formatDateTime(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(DATE);
    }

    /**
     * 时分秒格式的日期转化为字符串
     *
     * @return
     */
    public static String formatDateTime(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        return localTime.format(TIME);
    }

    /**
     * 年月日时分秒格式的日期转化为字符串
     *
     * @return
     */
    public static String formatDateTime(Date date) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        if (localDateTime == null) {
            return null;
        }
        return formatDateTime(localDateTime);
    }

    /**
     * 年月日时分秒格式的日期转化为字符串
     *
     * @return
     */
    public static String formatDateTime(Date date, DateTimeFormatter formatter) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        if (localDateTime == null) {
            return null;
        }
        return formatDateTime(localDateTime, formatter);
    }

    /**
     * 年月日时分秒格式的字符串转化为LocalDateTime
     *
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String str) {
        if (str == null) {
            return null;
        }
        return LocalDateTime.parse(str, DATETIME);
    }

    /**
     * 年月日格式的字符串转化为LocalDate
     *
     * @return
     */
    public static LocalDate parseLocalDate(String str) {
        if (str == null) {
            return null;
        }
        return LocalDate.parse(str, DATE);
    }

    /**
     * 时分秒格式的字符串转化为LocalTime
     *
     * @return
     */
    public static LocalTime parseLocalTime(String str) {
        if (str == null) {
            return null;
        }
        return LocalTime.parse(str, TIME);
    }

    /**
     * 年月日时分秒格式的字符串转化为Date
     *
     * @return
     */
    public static Date parseDateTime(String str) {
        if (str == null) {
            return null;
        }
        LocalDateTime localDateTime = parseLocalDateTime(str);
        if (localDateTime == null) {
            return null;
        }
        return toDate(localDateTime);
    }

    /**
     * 年月日格式的字符串转化为Date
     *
     * @return
     */
    public static Date parseDate(String str) {
        if (str == null) {
            return null;
        }
        LocalDate localDate = parseLocalDate(str);
        if (localDate == null) {
            return null;
        }
        return toDate(localDate);
    }

    /**
     * 时分秒格式的字符串转化为Date
     *
     * @return
     */
    public static Date parseTime(String str) {
        if (str == null) {
            return null;
        }
        LocalTime localTime = parseLocalTime(str);
        if (localTime == null) {
            return null;
        }
        return toDate(localTime);
    }

    /**
     * 获取当前星期几（1：星期一， 2：星期二）
     *
     * @return
     */
    public static int getDayOfWeek() {
        return LocalDateTime.now().getDayOfWeek().getValue();
    }

    /**
     * 获取今天是几号
     *
     * @return
     */
    public static int getDayOfMonth() {
        return LocalDateTime.now().getDayOfMonth();
    }

    /**
     * 获取秒数
     *
     * @return
     */
    public static Long getSecond() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取豪秒数
     *
     * @return
     */
    public static Long getMillsSecond() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 判断指定日期是否在日期区间内
     *
     * @return
     */
    public static boolean betweenDate(Date date, Date startDate, Date endDate) {
        if (null != startDate && null != endDate) {
            if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()) {
                return true;
            }
        } else if (null != startDate && null == endDate) {
            if (date.getTime() >= startDate.getTime()) {
                return true;
            }
        } else if (null == startDate && null != endDate) {
            if (date.getTime() <= endDate.getTime()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将星期数转化为对应的中文格式（1 = 周一，2 = 周二 ...）
     *
     * @return
     */
    public static String fomcatDayOfWeek(Integer weeks) {
        String value;
        switch (weeks) {
            case 1:
                value = "周一";
                break;
            case 2:
                value = "周二";
                break;
            case 3:
                value = "周三";
                break;
            case 4:
                value = "周四";
                break;
            case 5:
                value = "周五";
                break;
            case 6:
                value = "周六";
                break;
            case 7:
                value = "周日";
                break;
            default:
                value = "";
                break;
        }
        return value;
    }
}
