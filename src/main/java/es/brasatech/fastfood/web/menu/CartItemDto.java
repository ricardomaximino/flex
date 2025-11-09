package es.brasatech.fastfood.web.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record CartItemDto(
    String id,
    String itemId,
    String name,
    String description,
    String image,
    int quantity,
    Map<String, List<ProductCustomizer>> customizations,
    BigDecimal price
) { }

