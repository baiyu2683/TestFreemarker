package com.trs.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@ComponentScan("com.trs")
@EnableAutoConfiguration
@SpringBootApplication
@ImportResource("dubbo-consumer.xml")
public class Application {

	public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    } 
}
