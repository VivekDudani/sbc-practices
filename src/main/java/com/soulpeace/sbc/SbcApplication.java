package com.soulpeace.sbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan({"com.soulpeace.sbc.graphql", "com.soulpeace.sbc.data"})
public class SbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbcApplication.class, args);
    }

}
