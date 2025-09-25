package es.brasatech.fastfood.web.dto;

import java.math.BigDecimal;

public record MenuProduct(String id, String name, BigDecimal price, String description, String image, String[] customizations) {
}
