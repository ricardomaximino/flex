package es.brasatech.fastfood.domain;

import lombok.Data;

import java.util.List;

@Data
public class Product {

    // identifiers
    private String id;
    private String name;
    private List<String> alias;

    // finalPrice
    private Price price;

    // availability
    private boolean active;
    private int stock;

    // preparation
    private int preparationTime;
    private List<Item> items;

    // visualization
    private String image;
    private List<Group> groups;
    private String description;

    // ingredient
    private MeasurementUnit unit;
    private double shrinkage;

}
