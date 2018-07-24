package com.cardinal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * The Holidays observed at The Store.  Supplies utility methods for resolving their actual observed date based on year.
 */
public class Holidays {

    /**
     * Set containing Saturday and Sunday, which makeup the typical Weekend.
     */
    public static final Set<DayOfWeek> WEEKEND_DAYS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private static final MonthDay INDEPENDENCE_DAY = MonthDay.of(Month.JULY, 4);

    private Holidays() {
        // utility class, don't allow it to be built on accident
    }

    /**
     * Resolve the observed Independence Day.  It is statically tied to July 4th but may be observed Monday when it
     * falls on Sunday and Friday when it falls on Saturday.
     *
     * @param year the year to observe
     * @return the day July 4th would be observed, Sunday is observed on Monday, Saturday on Friday.
     */
    public static LocalDate resolveIndependenceDay(int year) {
        return resolveWeekendHoliday(year, INDEPENDENCE_DAY);
    }

    /**
     * Helper method for resolving weekend holidays.  Likely used in the future.
     *
     * @param year        the year to resolve
     * @param monthAndDay the month and day of the holiday normally
     * @return the day the holiday would be observed, Sunday is observed on Monday, Saturday on Friday.
     */
    private static LocalDate resolveWeekendHoliday(int year, MonthDay monthAndDay) {
        if (monthAndDay == null) {
            throw new IllegalArgumentException("Holiday not valid.");
        }

        LocalDate yearBasedHoliday = monthAndDay.atYear(year);
        switch (yearBasedHoliday.getDayOfWeek()) {
        case SUNDAY:
            // move it to Monday
            return yearBasedHoliday.plusDays(1);
        case SATURDAY:
            // move it to Friday
            return yearBasedHoliday.minusDays(1);
        default:
            return yearBasedHoliday;
        }
    }

    /**
     * Resolve Labor Day, the first Monday of every September.
     *
     * @param year the year to resolve
     * @return the first monday of September for the given year
     */
    public static LocalDate resolveLaborDay(int year) {
        // Technically Labor Day isn't recognized until 1887, however lets not code in something we'll never need
        LocalDate septemberFirst = LocalDate.of(year, Month.SEPTEMBER, 1);
        return septemberFirst.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    }

    /**
     * Calculate all holidays for the given year.  Allows new ones to be added later without refactoring everything.
     *
     * @param year the year to resolve holidays for
     * @return all observed holidays for the given year
     */
    public static List<LocalDate> resolveHolidays(int year) {
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(resolveLaborDay(year));
        holidays.add(resolveIndependenceDay(year));
        return holidays;
    }
}
