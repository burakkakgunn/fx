package com.fxexchange.fx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FxApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxApplication.class, args);
	}

}
