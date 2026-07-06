package com.aryan.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class InventoryManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            InventoryManagementSystemApplication.class,
            args
        );
    }
}