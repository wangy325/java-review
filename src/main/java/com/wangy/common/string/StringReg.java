package com.wangy.common.string;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * @author wangy
 * @date 2021-2-7 13:39
 */
@Slf4j
public class StringReg {
    static String pattern1 = "ab.cd";
    static String pattern2 = "ab.cd.ef";
    static String pattern3 = "java.util.String";
    static String pattern4 = "ok";

    static String pattern12(String reg) {
        boolean b1 = Pattern.matches(reg, pattern1);
        boolean b2 = Pattern.matches(reg, pattern2);
        boolean b3 = Pattern.matches(reg, pattern3);
        boolean b4 = Pattern.matches(reg, pattern4);
        return b1 + ", " + b2 + ", " + b3 + ", " + b4;
    }

    static String replaceFirst(String reg){
//        return pattern1.replaceFirst(reg, String.valueOf(pattern1.charAt(0)).toUpperCase());

        return Pattern.compile(reg).matcher(pattern1).replaceFirst(pattern1.substring(0,1).toUpperCase());
    }

    public static void main(String[] args) {
        log.info(pattern12("^([a-z]+\\.)+[a-z]+$"));
        log.info(replaceFirst("\\w"));
    }
}
