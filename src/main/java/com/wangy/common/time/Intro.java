package com.wangy.common.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple introduction of JDK 1.8's java.time.*
 *
 * @author wangy
 * @date 2021-2-24 15:35
 */
@SuppressWarnings("all")
public class Intro {
    static String DATE_TIME_FORM = "yyyy-MM-dd HH:mm:ss";

    static void parseDateTimeString(String yyyyMMdd_HHmmss){
        System.out.println(LocalDateTime.parse(yyyyMMdd_HHmmss));
    }

    static void parseDateTimeStringWithFormat(String yyyyMMdd_HHmmss){
        System.out.println(LocalDateTime.parse(yyyyMMdd_HHmmss, DateTimeFormatter.ofPattern(DATE_TIME_FORM)));
    }

    static void parseDateString(String yyyyMMdd){
        System.out.println(LocalDate.parse(yyyyMMdd));
    }

    static void parseTimeString(String HHmmss){
        System.out.println(LocalTime.parse(HHmmss));
    }

    public static void main(String[] args) {
        parseDateTimeString("2012-06-09T13:12:11");
        parseDateTimeStringWithFormat("2012-06-09 13:12:11");
        parseDateString("2012-06-06");
        parseTimeString("13:12:11");
    }
}
