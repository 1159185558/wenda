package com.example.wenda;

import com.example.wenda.asynchronization.EventHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.module.Configuration;

@SpringBootApplication
public class WendaApplication {

    public static void main(String[] args) {
        //SpringApplication.run(WendaApplication.class, args);
        SpringApplication springApplication = new SpringApplication(WendaApplication.class);
        ApplicationContext applicationContext = springApplication.run(args);

    }

}
