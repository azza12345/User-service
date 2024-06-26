package com.BudgetTracking.UserService;

import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Transactional
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
@ComponentScan
@ComponentScan(basePackages = {"com.BudgetTracking.UserService.Service", "com.BudgetTracking.UserService.repository"})
@ComponentScan(basePackages = "com.BudgetTracking.UserService.Controller")
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
