package es.brasatech.fastfood.domain;

import lombok.Getter;

@Getter
public enum MeasurementUnit {
    KILOGRAM("kg", 1000.0),
    GRAM("g", 1.0),
    LITER("L", 1000.0);

    private final String symbol;
    private final double baseConversionFactor;

    MeasurementUnit(String symbol, double baseConversionFactor) {
        this.symbol = symbol;
        this.baseConversionFactor = baseConversionFactor;
    }

}
