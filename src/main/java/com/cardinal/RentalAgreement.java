package com.cardinal;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Rental Agreement, capturing the rental as it was produced in case the price changes later.
 */
public class RentalAgreement {
    // including the tool directly, as it already has the structure.  will provide helpers as well
    private Tool rentedTool;
    private int rentalDays;
    private LocalDate checkoutDate;
    private int discountAsPercent;

    private LocalDate dueDate;
    private long chargeableDays;
    private BigDecimal totalDueBeforeDiscount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalDueAfterDiscount;

    /**
     * Construct a rental agreement with the core logic.
     *
     * @param rentedTool        the tool being rental
     * @param rentalDays        the number of days being rented
     * @param checkoutDate      the checkout date (start of rental)
     * @param discountAsPercent the discount for the contract total
     */
    public RentalAgreement(Tool rentedTool, int rentalDays, LocalDate checkoutDate, int discountAsPercent) {
        this.rentedTool = rentedTool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.discountAsPercent = discountAsPercent;
    }

    public String getToolCode() {
        return rentedTool.getToolCode();
    }

    public String getBrand() {
        return rentedTool.getBrand();
    }

    public ToolType getToolType() {
        return rentedTool.getToolType();
    }

    public BigDecimal getDailyCharge() {
        return rentedTool.getToolType().getDailyCharge();
    }

    public Tool getRentedTool() {
        return rentedTool;
    }

    public void setRentedTool(Tool rentedTool) {
        this.rentedTool = rentedTool;
    }


    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public int getDiscountAsPercent() {
        return discountAsPercent;
    }

    public void setDiscountAsPercent(int discountAsPercent) {
        this.discountAsPercent = discountAsPercent;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getChargeableDays() {
        return chargeableDays;
    }

    public void setChargeableDays(Long chargeableDays) {
        this.chargeableDays = chargeableDays;
    }

    public BigDecimal getTotalDueBeforeDiscount() {
        return totalDueBeforeDiscount;
    }

    public void setTotalDueBeforeDiscount(BigDecimal totalDueBeforeDiscount) {
        this.totalDueBeforeDiscount = totalDueBeforeDiscount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDueAfterDiscount() {
        return totalDueAfterDiscount;
    }

    public void setTotalDueAfterDiscount(BigDecimal totalDueAfterDiscount) {
        this.totalDueAfterDiscount = totalDueAfterDiscount;
    }
}
