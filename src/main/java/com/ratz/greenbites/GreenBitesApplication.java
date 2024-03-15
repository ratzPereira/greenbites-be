package com.ratz.greenbites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GreenBitesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenBitesApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        int BCRYPT_STRENGTH = 12;
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }
	
}
