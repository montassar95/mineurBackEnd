package com.cgpr.mineur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class MineurApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MineurApplication.class, args);
	}

}
 