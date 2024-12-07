package com.readjoy.readjoyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class ReadJoyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadJoyApiApplication.class, args);
    }

}
