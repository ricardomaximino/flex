package es.brasatech.fastfood.web.menu;

import java.util.List;

public record MenuData(List<MenuProduct> combos, List<MenuProduct> food, List<MenuProduct> drinks) {
}
