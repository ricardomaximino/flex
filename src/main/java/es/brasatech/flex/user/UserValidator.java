package es.brasatech.flex.user;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String CUSTOMER = "CUSTOMER";

    public boolean validate(User user) {
        // Implement user-specific validation rules
        switch (user.getType()) {
            case EMPLOYEE:
                return validateEmployeeFields(user);
            case CUSTOMER:
                return validateCustomerFields(user);
            default:
                return false;
        }
    }

    private boolean validateEmployeeFields(User user) {
        return (user.getCustomFields().containsKey("type") && EMPLOYEE.equals(user.getCustomFields().get("type")));
    }

    private boolean validateCustomerFields(User user) {
        return (user.getCustomFields().containsKey("type") && CUSTOMER.equals(user.getCustomFields().get("type")));
    }
}