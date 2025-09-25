package es.brasatech.fastfood;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Order {
    private String id;
    private String customerId;
    private String employeeId;
    private ZonedDateTime dateTime;
    private String status;
    private List<OrderItem> items;
    private double totalPrice;

}
