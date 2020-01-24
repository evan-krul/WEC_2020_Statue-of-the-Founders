package ca.wec2020.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Configuration
public class Application extends SpringBootServletInitializer {

//    Secured with https://github.com/vaadin-learning-center/spring-secured-vaadin
//    https://www.websparrow.org/spring/spring-boot-spring-security-with-jpa-authentication-and-mysql
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
