package es.brasatech.flex.shared;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
public class ValidatorManager <V extends Validator,T extends Flex> {
    private final Map<String, V> validatorMap;

    public ValidatorManager(List<V> validatorList) {
        validatorMap = validatorList.stream()
                .collect(Collectors.toMap(Validator::name, Function.identity()));

    }

    public void validate(T flex) {
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
