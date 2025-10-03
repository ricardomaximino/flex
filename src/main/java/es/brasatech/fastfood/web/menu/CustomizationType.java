package es.brasatech.fastfood.web.menu;

import lombok.Getter;

@Getter
public enum CustomizationType {
    SIZE("customizer.type.size"),
    ADD_EXTRA("customizer.type.add.extra"),
    REMOVE_ITEMS("customizer.type.remove.items"),
    TOGGLE_ITEMS("customizer.type.toggle.items");

    private final String key;

    CustomizationType(String key) {
        this.key = key;
    }
}
