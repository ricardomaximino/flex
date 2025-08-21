package es.brasatech.flex.user;

import es.brasatech.flex.data.Data;
import es.brasatech.flex.shared.Flex;
import es.brasatech.flex.shared.ValidationException;
import es.brasatech.flex.shared.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmployeeValidator implements Validator {
    private static final String NAME = "EMPLOYEE";
    private static final String ERROR = "error";
    private static final String WARN = "warn";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void validate(Flex flex) {
        validateEmployeeFields((Data) flex);
    }

    private void validateEmployeeFields(Data data) {
        var errorList = new ArrayList<String>();
        var warnList = new ArrayList<String>();
        var result = new HashMap<String, List<String>>();
        result.put(ERROR, errorList);
        result.put(WARN, warnList);
        var address = (Map<String, Object>) data.getCustomFields().get("address");
        var homeAddress = (Map<String, Object>) address.get("home");
        validateAddress(homeAddress, result);

        if(!result.get(ERROR).isEmpty() || !result.get(WARN).isEmpty()){
            var errorMessage = String.format("It is not a valid employee!! %n%s", result);
            log.error(errorMessage);
            throw new ValidationException(errorMessage, result);
        }
    }

    private void validateAddress(Map<String, Object> address, Map<String, List<String>> result){
        if(address.containsKey("country")){
            if(address.get("country") instanceof String country){
                if(country.isEmpty()){
                    result.get(ERROR).add("Country is mandatory and cannot be empty");
                }
                if(country.length() < 4) {
                    result.get(WARN).add("Country should have at least 4 characters");
                }
            }else {
                result.get(ERROR).add("Country must be a string");
            }
        }else {
            result.get(ERROR).add("Country is mandatory");
        }
    }

}
