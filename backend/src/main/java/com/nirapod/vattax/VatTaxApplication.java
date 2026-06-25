package com.nirapod.vattax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nirapod.dao")
@EnableAsync
@ComponentScan(basePackages = "com.nirapod")
@EntityScan(basePackages = {"com.nirapod"})
public class VatTaxApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(VatTaxApplication.class, args);
	}

}
