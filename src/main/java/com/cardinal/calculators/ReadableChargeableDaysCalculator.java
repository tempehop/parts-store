package com.cardinal.calculators;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.cardinal.Holidays;
import com.cardinal.RentalAgreement;
import com.cardinal.ToolType;

/**
 * Calculate the number of chargeable days for this rental agreement.
 */
public class ReadableChargeableDaysCalculator implements Function<RentalAgreement, Long> {

    private Map<Integer, List<LocalDate>> holidayCache = new HashMap<>();

    @Override
    public Long apply(RentalAgreement agreement) {
        // This isn't going to scale very well.  However a store is unlikely to rent a tool across a huge time span.
        // This code is significantly more readable, and with Java 9 we can iterate over the days more cleanly.
        // See PerformanceChargeableDaysCalculator for what it starts to look like when tuned for performance.

        // Test 5 indicates that we need to charge to the last date inclusive, and thus do not charge for the first day.
        // This means if you rent on friday and return on Sunday (2 elapsed days) that it'll not charge you.
        LocalDate checkoutDate = agreement.getCheckoutDate().plusDays(1);
        LocalDate finalDate = agreement.getDueDate();

        ToolType toolType = agreement.getRentedTool().getToolType();
        long rentalDays = agreement.getRentalDays();
        for (LocalDate current = checkoutDate; !current.isAfter(finalDate); current = current.plusDays(1)) {
            // lazily calculate the holidays for the year, in case we cross multiple years
            List<LocalDate> holidays = holidayCache.computeIfAbsent(current.getYear(), Holidays::resolveHolidays);
            if (toolType.noChargeOnWeekends() && Holidays.WEEKEND_DAYS.contains(current.getDayOfWeek())) {
                rentalDays = rentalDays - 1;
            } else if (toolType.noChargeOnHolidays() && holidays.contains(current)) {
                // if a holiday falls on a weekend, we don't want to double-deduct (not that it can now)
                rentalDays = rentalDays - 1;
            }
        }
        return rentalDays;
    }


}
