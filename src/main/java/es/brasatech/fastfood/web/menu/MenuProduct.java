package es.brasatech.fastfood.web.menu;

import java.math.BigDecimal;

public record MenuProduct(String id, String name, BigDecimal price, String description, String image, String[] customizations) {
    public String customizationsAsString() {
        if (customizations == null || customizations.length == 0) {
            return "";
        }
        return String.join(",", customizations);
    }
}
