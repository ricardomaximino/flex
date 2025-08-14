package es.brasatech.flex.user;

import es.brasatech.flex.shared.Validator;
import es.brasatech.flex.shared.ValidatorManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfiguration {

    @Bean
    public ValidatorManager<Validator, User> userValidatorManager(){
        return new ValidatorManager<>(List.of(new EmployeeValidator(), new CustomerValidator()));
    }
}
