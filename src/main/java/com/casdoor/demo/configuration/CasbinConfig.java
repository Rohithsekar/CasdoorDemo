package com.casdoor.demo.configuration;

import org.casbin.annotation.CasbinDataSource;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CasbinConfig {


    @Value("${casbin.model}")
    private String model;

    @Value("${casbin.policy}")
    private String policy;

    @Bean
    public Enforcer enforcer() {

        System.out.println("model path is " + model);
        System.out.println("policy path is " + policy);
        return new Enforcer(model, policy);
    }


    @Bean
    @CasbinDataSource
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource casbinDataSource() {
        return DataSourceBuilder.create().build();
    }



}
