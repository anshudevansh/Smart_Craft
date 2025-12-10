package com.email.SmartCraft.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration    // this is used since we are not creating constructor manually so we need to define a bean
                  // because Spring does not know how to create a WebCLient bean unless we explicitly tell it
                 // Spring does not need to autowire WebClient directly. It autowires the WebClient.Builder,
                   // which is already registered as a Spring-managed bean when you add the
               // spring-boot-starter-webflux dependency.
              //Since Spring knows how to create the builder, and you’re using constructor injection,
               //it happily resolves this during bean creation.
public class WebClientConfiguration {


    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

}
