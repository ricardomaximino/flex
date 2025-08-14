package es.brasatech.flex.user;

import es.brasatech.flex.shared.Flex;
import es.brasatech.flex.shared.ValidationException;
import es.brasatech.flex.shared.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerValidator implements Validator {
    private static final String NAME = "CUSTOMER";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void validate(Flex flex) {
        if(!NAME.equals(flex.getType())) {
            throw new ValidationException("It is not a customer!!");
        }
        if(!validateCustomerFields((User) flex)){
            throw new ValidationException("It is not a valid customer!!");
        }
    }
    private boolean validateCustomerFields(User user) {
        return (user.getCustomFields().containsKey("customerNumber"));
    }
}
