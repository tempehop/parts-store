package com.cardinal.calculators;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import com.cardinal.Holidays;
import com.cardinal.RentalAgreement;
import com.cardinal.ToolType;

/**
 * Ugly code, I wanted to see the performance difference for a straight math calculation.  Not used in the actual code.
 *
 * @deprecated because its faster, but uglier and much harder to understand
 */
@Deprecated
public class PerformanceChargeableDaysCalculator implements Function<RentalAgreement, Long> {

    @Override
    public Long apply(RentalAgreement agreement) {
        ToolType toolType = agreement.getRentedTool().getToolType();

        LocalDate checkoutDate = agreement.getCheckoutDate().plusDays(1);
        LocalDate returnDate = agreement.getDueDate().plusDays(1);

        long daysBetween = agreement.getRentalDays();
        if (toolType.noChargeOnWeekends()) {
            long weeks = checkoutDate.until(returnDate, ChronoUnit.WEEKS);
            if (weeks != 0) {
                daysBetween -= weeks * 2;
            }
            // we have some remainder
            if (agreement.getRentalDays() % 7 != 0) {
                if (checkoutDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    daysBetween -= 1;
                } else if (returnDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    daysBetween -= 1;
                } else if (returnDate.getDayOfWeek().getValue() < checkoutDate.getDayOfWeek().getValue()) {
                    daysBetween -= 2;
                }
            }
        }

        if (toolType.noChargeOnHolidays()) {
            for (int year = checkoutDate.getYear(); year <= returnDate.getYear(); year++) {
                if (isHolidayBetween(Holidays.resolveIndependenceDay(year), checkoutDate, returnDate)) {
                    daysBetween--;
                }

                if (isHolidayBetween(Holidays.resolveLaborDay(year), checkoutDate, returnDate)) {
                    daysBetween--;
                }
            }
        }

        return Math.max(0, daysBetween);
    }

    private boolean isHolidayBetween(LocalDate holiday, LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(holiday) && !endDate.isBefore(holiday);
    }
}
