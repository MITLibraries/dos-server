package edu.mit.dos;

import edu.mit.dos.model.Role;
import edu.mit.dos.model.User;
import edu.mit.dos.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Responsible for booting this Spring-based application
 */
@SpringBootApplication
public class SpringMain  {

    @Autowired (required = false)
    UserService userService;

    @Autowired(required = false)
    ServiceConfig config;


    public static void main(String[] args) {
        SpringApplication.run(SpringMain.class, args);
    }

   /* @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringMain.class);
    }*/

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // TODO Temporary. Remove when the database is set up with users
    @PostConstruct
    public void init() {
        System.out.println("Spring Boot Application Initializing . . .");

        if (userService == null) {
            System.err.println("Warning: UserService not injected");
            return;
        }

        if (config == null) {
            return;
        }

        if (config.getMode().equals("testing") == false) {
            return;
        }

        // TODO Temporary. Remove when the database is set up with users

        // Add test users (admin and client) if in the test mode

        System.out.println("Warning: Spring test mode. Adding users.");

        final User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("admin@email.com");
        admin.setRole(Role.ROLE_ADMIN);

        userService.signup(admin);

        final User client = new User();
        client.setUsername("client");
        client.setPassword("client");
        client.setEmail("client@email.com");
        client.setRole(Role.ROLE_CLIENT);

        userService.signup(client);

        System.out.println("Warning: Spring test mode. Users added.");


    }

}
