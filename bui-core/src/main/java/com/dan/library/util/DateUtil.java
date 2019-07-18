package com.dan.library.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bo on 2019/2/20 13:11
 */
public class DateUtil {

    /**
     * 毫秒转天
     */
    public static final long DAY_MILLI = 24 * 60 * 60 * 1000;

    /**
     * 英文 如：2010-12-01
     */
    public static final String FORMAT_SHORT = "yyyy-MM-dd";

    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static final String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static String getDateToString(String parse) {
        long millis = System.currentTimeMillis();
        if (StringUtils.isBlank(parse)) {
            parse = FORMAT_LONG;
        }
        SimpleDateFormat format = new SimpleDateFormat(parse);
        return format.format(millis);
    }

    /**
     * 计算2个日期间的相差天数
     *
     * @param date1 较大的时间
     * @param date2 较小的时间
     * @return 相差天数
     */
    public static long diffDay(Date date1, Date date2) {
        return (getMistiming(date1, date2) / DAY_MILLI);
    }

    public static long diffDay(String strStartDate, String strEndDate, String pattern) {

        return (getMistiming(strStartDate, strEndDate, pattern) / DAY_MILLI);
    }

    /**
     * 获取时间差 毫秒
     *
     * @param strStartDate 开始时间
     * @param strEndDate   结束时间
     * @param pattern      时间格式
     * @return 毫秒数
     */
    public static Long getMistiming(String strStartDate, String strEndDate, String pattern) {

        return getMistiming(parseToDate(strStartDate, pattern), parseToDate(strEndDate, pattern));
    }


    /**
     * 时间差 默认格式 yyyy-MM-dd HH:mm:ss
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间差
     */
    public static Long getMistiming(Date startDate, Date endDate) {

        return (endDate.getTime() - startDate.getTime());
    }

    /**
     * 使用参数Format将字符串转为Date
     *
     * @param strDate String时间字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date parseToDate(String strDate, String pattern) {
        try {
            return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
        } catch (ParseException e) {
            System.out.println(strDate + "转换Date失败...格式:" + pattern);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date转为字符串String
     *
     * @param date yyyy-MM-dd HH:mm:ss
     * @return 字符串
     */
    public static String parseToString(Date date) {

        return date == null ? "" : parseToString(date, FORMAT_LONG);
    }

    /**
     * 使用参数Date转为字符串String，指定时间格式
     *
     * @return 字符串
     */
    public static String parseToString(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

}
