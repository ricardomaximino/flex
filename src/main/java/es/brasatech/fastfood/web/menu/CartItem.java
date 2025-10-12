package es.brasatech.fastfood.web.menu;

import java.math.BigDecimal;
import java.util.List;

public record CartItem(String id, String productId, int quantity, List<ProductCustomizer> customizations, BigDecimal price) {
}
