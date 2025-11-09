package es.brasatech.fastfood.web.menu;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto (
    String id,
    LocalDateTime dateTime,
    Double tax,
    BigDecimal taxAmount,
    List<CartItemDto> itemList,
    BigDecimal subtotal,
    BigDecimal total
) { }
