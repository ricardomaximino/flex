package es.brasatech.flex.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Component
public class ValidatorManager {
    private final Map<String, Validator> validatorMap;

    public ValidatorManager(List<Validator> validatorList) {
        validatorMap = validatorList.stream()
                .collect(Collectors.toMap(Validator::name, Function.identity()));
    }

    public void validate(Flex flex) {
        var validator = validatorMap.get(flex.getType());
        if (validator != null) {
            validator.validate(flex);
        }else {
            var errorMessage = String.format("The type [ %s ] is not supported", flex.getType());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
