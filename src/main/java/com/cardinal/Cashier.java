package com.cardinal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.cardinal.calculators.ReadableChargeableDaysCalculator;

/**
 * Cashier application acts as a register.
 */
public class Cashier {
    private final ToolCatalogDto availableToolDto;

    public Cashier() {
        availableToolDto = new ToolCatalogDto();
    }

    /**
     * Checkout routine, used to
     *
     * @param toolCode          the tool code to checkout
     * @param rentalDays        the days to rent the tool
     * @param discountAsPercent the discount as a percentage
     * @param checkoutDate      the checkout date
     * @return a fully calculated RentalAgreement
     */
    public RentalAgreement checkout(String toolCode, int rentalDays, int discountAsPercent, LocalDate checkoutDate) {
        if (rentalDays <= 0) {
            throw new IllegalArgumentException("You must rent a tool for at least one day. You provided: " + rentalDays);
        }

        if (discountAsPercent < 0 || discountAsPercent > 100) {
            throw new IllegalArgumentException("Discount cannot exceed 100% or fall below 0%.  You provided: " + discountAsPercent + "%");
        }

        if (checkoutDate == null) {
            // not required by the spec, but probably good without external validation in place
            throw new IllegalArgumentException("You must provide a checkout date.");
        }

        Tool tool = availableToolDto.fetchToolByCode(toolCode)
                .orElseThrow(() -> {
                    // not strictly required by the spec, but good to have in place.
                    String availableTools = availableToolDto.fetchToolsFromCatalog().stream()
                            .map(Tool::getToolCode)
                            .collect(Collectors.joining(", "));

                    String toolProvided = "\nYou provided: '" + toolCode + "'.";
                    if (toolCode == null) {
                        toolProvided = "\nYou did not provide a tool code.";
                    }

                    return new IllegalArgumentException("That tool code was not found in our catalog." +
                            toolProvided +
                            "\nAvailable: " + availableTools);
                });

        RentalAgreement rentalAgreement = new RentalAgreement(tool, rentalDays, checkoutDate, discountAsPercent);

        // calculate a few things
        rentalAgreement.setDueDate(checkoutDate.plusDays(rentalDays));

        Long chargeableDays = new ReadableChargeableDaysCalculator().apply(rentalAgreement);
        rentalAgreement.setChargeableDays(chargeableDays);

        BigDecimal totalCharge = tool.getToolType().getDailyCharge().multiply(BigDecimal.valueOf(chargeableDays));
        rentalAgreement.setTotalDueBeforeDiscount(totalCharge);

        BigDecimal totalDiscount = totalCharge
                .multiply(BigDecimal.valueOf(discountAsPercent, 2))
                .setScale(2, RoundingMode.HALF_UP);
        rentalAgreement.setTotalDiscountAmount(totalDiscount);

        rentalAgreement.setTotalDueAfterDiscount(totalCharge.subtract(totalDiscount));

        return rentalAgreement;
    }
}
