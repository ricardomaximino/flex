package es.brasatech.fastfood.web.dto;

import java.util.List;

public record MenuData(List<MenuProduct> combos, List<MenuProduct> food, List<MenuProduct> drinks) {
}
