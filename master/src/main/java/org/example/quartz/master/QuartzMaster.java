package org.example.quartz.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.example.quartz")
public class QuartzMaster {
    public static void main(String[] args) {
        SpringApplication.run(QuartzMaster.class, args);
    }
}
