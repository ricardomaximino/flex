package es.brasatech.fastfood.web.dto;

public enum CustomizationInputType {
    RADIO("radio"),
    CHECKBOX("checkbox");

    private final String value;

    CustomizationInputType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
