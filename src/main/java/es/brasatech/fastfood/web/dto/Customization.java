package es.brasatech.fastfood.web.dto;

import java.util.List;

public record Customization(String id, CustomizationType type, String name, CustomizationInputType inputType, List<CustomizationOption> options) {
}
