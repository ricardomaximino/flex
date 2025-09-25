package es.brasatech.fastfood;

import java.math.BigDecimal;

public record ProductDto(
    // identifiers
    String id,
    String name,
    String alias,

    // Price
    Price price,

    // availability
    boolean active,
    int stock,

    // preparation
    int preparationTime,
    String items,

    // visualization
    String image,
    String groups,
    String description,

    // ingredient
    MeasurementUnit unit,
    double shrinkage,
    BigDecimal cost,
    BigDecimal marginOfBenefit,
    BigDecimal tax
) {
    public static ProductDto empty(){
        return new ProductDto(null, null, null, null, false, 0, 0, null, null, null, null, null, 0, null, null, null);
    }
}
