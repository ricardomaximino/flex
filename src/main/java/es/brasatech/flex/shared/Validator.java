package es.brasatech.flex.shared;

public interface Validator {
    String name();
    void validate(Flex flex);
}