package com.xinpeng.mp3projectfr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.xinpeng.mp3projectfr") // Ensure this is pointing to your repository package
@EntityScan(basePackages = "com.xinpeng.mp3projectfr") // Ensure
public class Mp3projectFrApplication {
    public static void main(String[] args) {
        SpringApplication.run(Mp3projectFrApplication.class, args);
    }
}
