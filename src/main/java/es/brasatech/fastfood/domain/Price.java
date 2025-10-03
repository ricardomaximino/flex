package es.brasatech.fastfood.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Price (
    BigDecimal cost,
    BigDecimal marginOfBenefit,
    BigDecimal tax,
    BigDecimal promotionalPrice,
    BigDecimal finalPrice,
    boolean sellable,
    PriceMode priceMode,
    boolean propagatePriceChange){

    public BigDecimal finalPrice() {
        // finalPrice = cost × (1 + tax / 100) × (1 + margin / 100)
        // Convert percentages to multipliers
        BigDecimal taxMultiplier = BigDecimal.ONE.add(tax.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal marginMultiplier = BigDecimal.ONE.add(marginOfBenefit.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));

        // Final finalPrice calculation
        return cost.multiply(taxMultiplier).multiply(marginMultiplier);
    }

    public BigDecimal margin() {
        // margin = [(finalPrice / (cost × (1 + tax / 100))) - 1] × 100
        // Convert percentages to multipliers
        BigDecimal taxMultiplier = BigDecimal.ONE.add(tax.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal baseWithTax = cost.multiply(taxMultiplier);
        BigDecimal marginMultiplier = finalPrice.divide(baseWithTax, 4, RoundingMode.HALF_UP);

        //Calculate margin percentage
        return marginMultiplier.subtract(BigDecimal.ONE).multiply(new BigDecimal("100"));
    }
}
