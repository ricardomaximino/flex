package es.brasatech.fastfood.web.menu;

import java.util.List;

public record CartDto(List<CartItemDto> cartItemList) {
}
