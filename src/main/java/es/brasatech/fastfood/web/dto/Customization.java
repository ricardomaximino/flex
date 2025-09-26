package es.brasatech.fastfood.web.dto;

import java.util.List;

public record Customization(String id, String name, String type, List<CustomizationOption> options) {
}
