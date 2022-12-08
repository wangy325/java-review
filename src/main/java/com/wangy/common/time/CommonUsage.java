package com.wangy.common.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * {@link java.time} 是jdk8 新增的日期时间包，可以完全替代之前的日期时间系统，包括：
 *
 * <li>{@link Date}</li>
 * <li>{@link java.util.Calendar}</li>
 * <li>{@link java.util.TimeZone}</li>
 * <li>{@link java.text.DateFormat}</li>
 * <li>{@link SimpleDateFormat}</li>
 * <p>
 * {@link java.time}是全新设计的api，应学习如何使用之。
 * <p>
 * talk1: <a href="https://stackoverflow.com/questions/27454025/unable-to-obtain-localdatetime-from-temporalaccessor-when-parsing-localdatetime">...</a>
 *
 * @author wangy
 * @date 2021-2-2 9:47
 * @see LocalDateTime
 */
public class CommonUsage {
    static String yyyy = "yyyy";
    static String yyyy_MM = "yyy-MM";
    static String yyyy_MM_dd = "yyyy-MM-dd";
    static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    static String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * If the pattern like 'yyyy', result {@link LocalDateTime} could be like 'yyyy-01-01 00:00:00'.<br>
     * If the pattern like 'yyyy-MM', result {@link LocalDateTime} could be like 'yyyy-MM-01 00:00:00'.<br>
     * If the pattern like 'yyyy-MM-dd', result {@link LocalDateTime} could be like 'yyyy-MM-dd 00:00:00'.<br>
     * Other patterns acts the same.
     * <p>
     *
     * <b>Important:</b> the pattern and the the {@link LocalDateTime#parse(CharSequence, DateTimeFormatter)} method's input {@link CharSequence} must match.
     * e.g. The following method call will throw {@link java.time.format.DateTimeParseException}:
     * <pre>
     *      LocalDateTime localDateTime = LocalDateTime.parse("2021",dtfBuilder("yyyy-MM"));
     * </pre>
     *
     * @param pattern the string pattern
     * @see DateTimeFormatter javadoc
     * @see LocalDateTime#parse(CharSequence, DateTimeFormatter)
     */
    static DateTimeFormatter dtfBuilder(String pattern) {
        return new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .parseDefaulting(ChronoField.YEAR_OF_ERA, LocalDateTime.now().getYear())
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .toFormatter();
    }

    static SimpleDateFormat sdfBuilder(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * solution1:
     * Get yyyy-MM-dd 00:00:00, start of the day.<br>
     * If you use yyyy-MM-dd as parameter
     */
    static Date getStartOfDay() {
        LocalDateTime localDateTime = LocalDateTime.parse("2021-02-02",
                dtfBuilder(yyyy_MM_dd));
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * solution2:
     * get yyyy-MM-dd 00:00:00, start of the day.<br>
     * If you use yyyy-MM-dd as parameter<br>
     * <p>
     * Using {@link LocalDate#atStartOfDay()}
     *
     * @param dateString datePattern like 2012-09-08
     */
    static Date getStartOfDay(String dateString) throws ParseException {
        //由于dtfBuilder的设置 这里的时间已经是 00:00:00
        LocalDate localDate = LocalDate.parse(dateString, dtfBuilder(yyyy_MM_dd));
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
//        return sdfBuilder().parse(zonedDateTime.format(dtfBuilder(yyyy_MM_dd_HH_mm_ss)));
    }

    /**
     *
     */
    static Date getStartOfDay(LocalDate localDate){
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());

        LocalDateTime localDateTime = localDate.atStartOfDay();
        Instant instant = localDateTime.toInstant(ZoneOffset.of("+8"));
//        return Date.from(zonedDateTime.toInstant());
        return Date.from(instant);
    }

    /**
     * Get yyyy-MM-dd 23:59:59.999, end of the day.<br>
     * By using {@link LocalDateTime#plus(long, TemporalUnit)}
     */
    static Date getEndOfDay() {
        //由于dtfBuilder的设置 这里的时间已经是 00:00:00
        LocalDateTime localDateTime = LocalDateTime.parse("2020-12-21", dtfBuilder(yyyy_MM_dd));

        //plusXxx()方法没有提供毫秒/微秒的相应方法，直接到纳秒； 相应的，可以使用plus方法指定单位
        localDateTime = localDateTime.plusHours(23).plusMinutes(59).plusSeconds(59).plusNanos(999_999_999);

        /*
         *  好方法，将其下级单位的值清零
         *  ChronoUnit.DAYS: 清零hh:mm:ss.SSS
         *  最大单位 DAYS，也就是说此方法只能用来清零时间
         */
        localDateTime = localDateTime.truncatedTo(ChronoUnit.DAYS);
        localDateTime = localDateTime
                .plus(23, ChronoUnit.HOURS)
                .plus(59, ChronoUnit.MINUTES)
                .plus(59, ChronoUnit.SECONDS)
                .plus(999, ChronoUnit.MILLIS);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Get yyyy-MM-dd 23:59:59.999, end of the day.<br>
     * By using {@link LocalDate#atTime(LocalTime)}
     *
     * @param dateString date pattern like '2012-09-18'
     */
    static Date getEndOfDay(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString, dtfBuilder(yyyy_MM_dd));
//        localDate.
        LocalDateTime localDateTime = localDate.atTime(23, 59, 59, 999_999_999);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    /**
     * Get first day of month
     *
     * @param dateString pattern like '2020-10-25'
     * @return
     */
    static Date getFirstDayOfMonth(String dateString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, dtfBuilder(yyyy_MM_dd));
        localDateTime = localDateTime.withDayOfMonth(1);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Get the first day of year.
     *
     * @param yearString pattern like '2012'
     */
    static Date getFirstDayOfYear(String yearString) {
        LocalDateTime localDateTime = LocalDateTime.parse(yearString, dtfBuilder(yyyy));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static void main(String[] args) throws ParseException {
        System.out.println(sdfBuilder(yyyy_MM_dd_HH_mm_ss).format(getStartOfDay()));
        System.out.println(sdfBuilder(yyyy_MM_dd_HH_mm_ss).format(getStartOfDay("2021-02-02")));
        System.out.println(sdfBuilder(yyyy_MM_dd_HH_mm_ss_SSS).format(getStartOfDay(LocalDate.parse("2021-02-22", dtfBuilder(yyyy_MM_dd)))));
        System.out.println(sdfBuilder(yyyy_MM_dd_HH_mm_ss_SSS).format(getEndOfDay()));
        System.out.println(sdfBuilder(yyyy_MM_dd_HH_mm_ss_SSS).format(getEndOfDay("2020-12-21")));
        System.out.println(sdfBuilder(yyyy_MM_dd).format(getFirstDayOfMonth("2020-12-18")));
        System.out.println(sdfBuilder(yyyy_MM_dd_HH_mm_ss).format(getFirstDayOfYear("2012")));
    }
}
