package com.moderngas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.moderngas"})
public class ModerngasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModerngasApplication.class, args);
    }

}
