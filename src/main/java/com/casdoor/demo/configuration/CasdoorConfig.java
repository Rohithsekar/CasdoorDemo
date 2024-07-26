package com.casdoor.demo.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.config.Config;
import org.casbin.casdoor.service.ApplicationService;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.RoleService;
import org.casbin.casdoor.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Getter
@Slf4j
public class CasdoorConfig {

    @Value("${casdoor.endpoint}")
    private String endpoint;

    @Value("${casdoor.client-id}")
    private String clientId;

    @Value("${casdoor.client-secret}")
    private String clientSecret;

    @Value("${casdoor.jwt-public-key}")
    private Resource jwtPublicKeyResource;

    @Value("${casdoor.organization}")
    private String organizationName;

    @Value("${casdoor.application-name}")
    private String applicationName;

    private final ResourceLoader resourceLoader;

    public CasdoorConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Bean
    public Config config() {

        try {
            String jwtPublicKey = StreamUtils.copyToString(jwtPublicKeyResource.getInputStream(), StandardCharsets.UTF_8);
//            System.out.println("public key is " + jwtPublicKey);
            return new Config(endpoint, clientId, clientSecret, jwtPublicKey, organizationName, applicationName);
        } catch (IOException e) {

            String message = String.format("IOException caught while configuring casdoor: %s", e.getMessage());
            log.error(message);
            throw new RuntimeException(message);
        }


    }


    @Bean
    public AuthService authService() {
        return new AuthService(config());
    }

    @Bean
    public UserService userService() {
        return new UserService(config());
    }

    @Bean
    public RoleService roleService() {
        return new RoleService(config());
    }

    @Bean
    public ApplicationService applicationService(){return new ApplicationService(config());}
}
