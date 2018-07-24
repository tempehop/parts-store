package com.cardinal;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.Test;

import static org.junit.Assert.*;

public class HolidaysTest {

    // Labor Days pulled from wikipedia: https://en.wikipedia.org/wiki/Labor_Day#Dates

    @Test
    public void laborDay_startOfMonth() {
        LocalDate actual = Holidays.resolveLaborDay(2008);
        LocalDate expected = LocalDate.of(2008, 9, 1);
        assertEquals("In 2008, the first monday is the first of the month", expected, actual);
    }

    @Test
    public void laborDay_lastPossible() {
        LocalDate actual = Holidays.resolveLaborDay(2015);
        LocalDate expected = LocalDate.of(2015, 9, 7);
        assertEquals("In 2015, the first monday is the latest it can be.", expected, actual);
    }

    @Test
    public void laborDay_thisYear() {
        LocalDate actual = Holidays.resolveLaborDay(2018);
        LocalDate expected = LocalDate.of(2018, 9, 3);
        assertEquals("In 2018, the first monday is in the middle.", actual, expected);
    }

    @Test
    public void independenceDay_Friday() {
        LocalDate actual = Holidays.resolveIndependenceDay(2014);
        assertEquals(LocalDate.of(2014, 7, 4), actual);
        assertEquals(DayOfWeek.FRIDAY, actual.getDayOfWeek());
    }

    @Test
    public void independenceDay_SaturdayObservedFriday() {
        LocalDate actual = Holidays.resolveIndependenceDay(2009);
        assertEquals(LocalDate.of(2009, 7, 3), actual);
        assertEquals(DayOfWeek.FRIDAY, actual.getDayOfWeek());
    }

    @Test
    public void independenceDay_SundayObservedMonday() {
        LocalDate actual = Holidays.resolveIndependenceDay(2010);
        assertEquals(LocalDate.of(2010, 7, 5), actual);
        assertEquals(DayOfWeek.MONDAY, actual.getDayOfWeek());
    }

    @Test
    public void independenceDay_Monday() {
        LocalDate actual = Holidays.resolveIndependenceDay(2011);
        assertEquals(LocalDate.of(2011, 7, 4), actual);
        assertEquals(DayOfWeek.MONDAY, actual.getDayOfWeek());
    }

}