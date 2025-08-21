package es.brasatech.flex.user;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.shared.Flex;
import es.brasatech.flex.shared.ValidationException;
import es.brasatech.flex.shared.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PropertyValidator implements Validator {
    private static final String NAME = "PROPERTY";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void validate(Flex flex) {
        if(!NAME.equals(flex.getType())) {
            throw new ValidationException("It is not a property!!");
        }
        if(!validateCustomerFields((Data) flex)){
            throw new ValidationException("It is not a valid property!!");
        }
    }
    private boolean validateCustomerFields(Data data) {
        return (data.getCustomFields().containsKey("propertyNumber"));
    }
}
