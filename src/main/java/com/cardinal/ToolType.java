package com.cardinal;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The type of tool to rent out.  Includes information on charges and excluded days.
 * <p>
 * TODO: This should come from a datasource to allow the store to grow.
 */
public enum ToolType {
    LADDER(BigDecimal.valueOf(1.99), false, false),
    CHAINSAW(BigDecimal.valueOf(1.49), true, false),
    JACKHAMMER(BigDecimal.valueOf(2.99), true, true);

    private final BigDecimal dailyCharge;
    private final boolean noDailyChargeOnWeekends;
    private final boolean noDailyChargeOnHolidays;

    ToolType(BigDecimal dailyCharge, boolean noDailyChargeOnWeekends, boolean noDailyChargeOnHolidays) {
        this.dailyCharge = dailyCharge.setScale(2, RoundingMode.HALF_UP);
        this.noDailyChargeOnWeekends = noDailyChargeOnWeekends;
        this.noDailyChargeOnHolidays = noDailyChargeOnHolidays;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public boolean noChargeOnWeekends() {
        return noDailyChargeOnWeekends;
    }

    public boolean noChargeOnHolidays() {
        return noDailyChargeOnHolidays;
    }
}
