package es.brasatech.flex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"es.brasatech.flex", "es.brasatech.fastfood"})
public class FlexApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlexApplication.class, args);
	}

}
