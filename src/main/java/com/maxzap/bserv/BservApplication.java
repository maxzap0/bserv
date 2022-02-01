package com.maxzap.bserv;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log
@SpringBootApplication
public class BservApplication {

    public static void main(String[] args) {
        SpringApplication.run(BservApplication.class, args);
        log.info("<<<---RUN--->>>");

    }

}
