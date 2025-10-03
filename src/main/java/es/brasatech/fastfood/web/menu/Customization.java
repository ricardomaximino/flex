package es.brasatech.fastfood.web.menu;

import java.util.List;

public record Customization(String id, CustomizationType type, String name, CustomizationInputType inputType, List<CustomizationOption> options) {
}
