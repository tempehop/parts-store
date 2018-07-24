package com.cardinal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CashierTest {
    private Cashier cashier = new Cashier();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    // Test 1
    @Test
    public void checkoutInvalidDiscount() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Discount cannot exceed 100% or fall below 0%.  You provided: 101%");

        LocalDate checkoutExpected = LocalDate.of(2015, 9, 3);
        cashier.checkout("JAKR", 5, 101, checkoutExpected);

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Discount cannot exceed 100% or fall below 0%.  You provided: -1%");

        checkoutExpected = LocalDate.of(2015, 9, 3);
        cashier.checkout("JAKR", 5, -1, checkoutExpected);
    }

    // Test 2
    @Test
    public void checkoutLadderInSeptemberWithWeekendAndLaborDay() {
        LocalDate checkoutExpected = LocalDate.of(2015, 9, 3);

        RentalAgreement rentalAgreement = cashier.checkout("LADW", 5, 10, checkoutExpected);

        assertInputEquals("LADW", 5, 10, checkoutExpected, rentalAgreement);

        assertEquals(LocalDate.of(2015, 9, 8), rentalAgreement.getDueDate());
        assertMoneyEquals(1.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(5), rentalAgreement.getChargeableDays());
        assertMoneyEquals(9.95, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(1.00, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(8.95, rentalAgreement::getTotalDueAfterDiscount);
    }

    // Test 3
    @Test
    public void checkoutChainsawInJulyWithHolidayAndWeekend() {
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);

        RentalAgreement rentalAgreement = cashier.checkout("CHNS", 5, 25, checkoutDate);

        assertInputEquals("CHNS", 5, 25, checkoutDate, rentalAgreement);

        assertEquals(LocalDate.of(2015, 7, 7), rentalAgreement.getDueDate());
        assertMoneyEquals(1.49, rentalAgreement::getDailyCharge);
        assertEquals(new Long(3), rentalAgreement.getChargeableDays());
        assertMoneyEquals(4.47, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(1.12, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(3.35, rentalAgreement::getTotalDueAfterDiscount);
    }

    // Test 4
    @Test
    public void checkoutJackhammerWithHolidayAndWeekend() {
        LocalDate checkoutExpected = LocalDate.of(2015, 9, 3);

        RentalAgreement rentalAgreement = cashier.checkout("JAKD", 6, 0, checkoutExpected);

        assertInputEquals("JAKD", 6, 0, checkoutExpected, rentalAgreement);

        assertEquals(LocalDate.of(2015, 9, 9), rentalAgreement.getDueDate());
        assertMoneyEquals(2.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(3), rentalAgreement.getChargeableDays());
        assertMoneyEquals(8.97, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(8.97, rentalAgreement::getTotalDueAfterDiscount);
    }

    // Test 5
    @Test
    public void checkoutJackhammerWithHolidayAndTwoWeekends() {
        LocalDate checkoutExpected = LocalDate.of(2015, 7, 2);

        RentalAgreement rentalAgreement = cashier.checkout("JAKR", 9, 0, checkoutExpected);

        assertInputEquals("JAKR", 9, 0, checkoutExpected, rentalAgreement);

        assertEquals(LocalDate.of(2015, 7, 11), rentalAgreement.getDueDate());
        assertMoneyEquals(2.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(5), rentalAgreement.getChargeableDays());
        assertMoneyEquals(14.95, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(14.95, rentalAgreement::getTotalDueAfterDiscount);
    }

    // Test 6
    @Test
    public void checkoutJackhammerWithHolidayAndReturnOnWeekend() {
        LocalDate checkoutExpected = LocalDate.of(2020, 7, 2);

        RentalAgreement rentalAgreement = cashier.checkout("JAKR", 4, 50, checkoutExpected);

        assertInputEquals("JAKR", 4, 50, checkoutExpected, rentalAgreement);

        assertEquals(LocalDate.of(2020, 7, 6), rentalAgreement.getDueDate());
        assertMoneyEquals(2.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(1), rentalAgreement.getChargeableDays());
        assertMoneyEquals(2.99, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(1.50, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(1.49, rentalAgreement::getTotalDueAfterDiscount);
    }

    // Extra Tests

    @Test
    public void checkoutInvalidRentalDays() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You must rent a tool for at least one day. You provided: 0");

        LocalDate checkoutExpected = LocalDate.of(2015, 9, 3);
        cashier.checkout("JAKR", 0, 10, checkoutExpected);

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You must rent a tool for at least one day.  You provided: -1");

        checkoutExpected = LocalDate.of(2015, 9, 3);
        cashier.checkout("JAKR", -1, 10, checkoutExpected);
    }

    @Test
    public void checkoutWithInvalidDate() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("You must provide a checkout date.");

        cashier.checkout("JAKR", 1, 10, null);
    }

    @Test
    public void checkoutWithInvalidToolCode() {
        String availableTools = "LADW, CHNS, JAKR, JAKD";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("That tool code was not found in our catalog." +
                "\nYou did not provide a tool code." +
                "\nAvailable: " + availableTools);

        cashier.checkout(null, 1, 10, LocalDate.now());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("That tool code was not found in our catalog." +
                "\nYou provided: LHKD." +
                "\nAvailable: " + availableTools);

        cashier.checkout("LHKD", 1, 10, LocalDate.now());
    }

    @Test
    public void checkoutJackhammerWithJuly4thOnSunday() {
        // 2010-07-04 is on a sunday
        LocalDate checkoutExpected = LocalDate.of(2010, 7, 3);

        RentalAgreement rentalAgreement = cashier.checkout("JAKR", 5, 0, checkoutExpected);

        assertInputEquals("JAKR", 5, 0, checkoutExpected, rentalAgreement);

        assertMoneyEquals(2.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(3), rentalAgreement.getChargeableDays());
        assertMoneyEquals(8.97, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(8.97, rentalAgreement::getTotalDueAfterDiscount);
    }

    @Test
    public void checkoutJackhammerWithNoChargeableDays() {
        LocalDate checkoutExpected = LocalDate.of(2015, 7, 3);

        RentalAgreement rentalAgreement = cashier.checkout("JAKR", 2, 0, checkoutExpected);

        assertInputEquals("JAKR", 2, 0, checkoutExpected, rentalAgreement);

        assertMoneyEquals(2.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(0), rentalAgreement.getChargeableDays());
        assertMoneyEquals(0.00, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDueAfterDiscount);
    }

    @Test
    public void checkoutChainsawAcrossYears() {
        LocalDate checkoutExpected = LocalDate.of(2015, 1, 1);

        // 2 x 365 = 730
        RentalAgreement rentalAgreement = cashier.checkout("JAKR", 730, 0, checkoutExpected);

        assertInputEquals("JAKR", 730, 0, checkoutExpected, rentalAgreement);

        // 2015/01/02 = Friday (First day of Charge)
        // 2017/01/01 = Sunday
        // four holidays

        // 730 - (52 weeks * 2 weekend days * 2 years) = 522
        // 522 - 1 for 2017/01/01 on Sunday = 521
        // 521 - 4 holidays = 517

        assertMoneyEquals(2.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(517), rentalAgreement.getChargeableDays());
        assertMoneyEquals(1545.83, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(1545.83, rentalAgreement::getTotalDueAfterDiscount);
    }

    @Test
    public void checkoutFullDiscount() {
        LocalDate checkoutExpected = LocalDate.of(2015, 9, 3);

        RentalAgreement rentalAgreement = cashier.checkout("LADW", 5, 100, checkoutExpected);

        assertInputEquals("LADW", 5, 100, checkoutExpected, rentalAgreement);

        assertMoneyEquals(1.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(5), rentalAgreement.getChargeableDays());
        assertMoneyEquals(9.95, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(9.95, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDueAfterDiscount);
    }

    @Test
    public void checkoutSingleDay() {
        LocalDate checkoutExpected = LocalDate.of(2015, 9, 3);

        RentalAgreement rentalAgreement = cashier.checkout("LADW", 1, 0, checkoutExpected);

        assertInputEquals("LADW", 1, 0, checkoutExpected, rentalAgreement);

        assertMoneyEquals(1.99, rentalAgreement::getDailyCharge);
        assertEquals(new Long(1), rentalAgreement.getChargeableDays());
        assertMoneyEquals(1.99, rentalAgreement::getTotalDueBeforeDiscount);
        assertMoneyEquals(0.00, rentalAgreement::getTotalDiscountAmount);
        assertMoneyEquals(1.99, rentalAgreement::getTotalDueAfterDiscount);
    }

    private void assertMoneyEquals(double value, Supplier<BigDecimal> actualValueSupplier) {
        assertEquals(new BigDecimal(value).setScale(2, RoundingMode.HALF_UP), actualValueSupplier.get());
    }

    private void assertInputEquals(String toolCode, int rentalDays, int discountAsPercent, LocalDate checkout,
                                   RentalAgreement agreement) {

        assertNotNull(agreement);

        assertEquals(toolCode, agreement.getToolCode());
        assertEquals(rentalDays, agreement.getRentalDays());
        assertEquals(discountAsPercent, agreement.getDiscountAsPercent());
        assertEquals(checkout, agreement.getCheckoutDate());
    }

}

