package com.casdoor.demo;

import com.casdoor.demo.entity.ApplicationUser;
import com.casdoor.demo.repository.ApplicationUserRepository;
import org.casbin.casdoor.config.CasdoorAutoConfigure;
import org.casbin.casdoor.entity.Application;
import org.casbin.casdoor.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication(exclude = CasdoorAutoConfigure.class)
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ApplicationService applicationService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<ApplicationUser> applicationUserList = applicationUserRepository.findAll();

        applicationUserList.forEach(user ->
                System.out.println(user.toString()));

        if(applicationService!=null){
            System.out.println(" service not null..");
        }
        Application application = applicationService.getApplication("rakaz-app");

        System.out.println(" application name is " + application.name);
        System.out.println(" application client id is " + application.clientId);
        System.out.println(" application client secret is " + application.clientSecret);
        System.out.println(" application grant type is " + application.grantTypes);


    }
}
