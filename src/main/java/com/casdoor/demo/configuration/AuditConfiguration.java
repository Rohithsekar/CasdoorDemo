package com.casdoor.demo.configuration;

import com.casdoor.demo.security.SpringSecurityAuditorAware;
import org.casbin.casdoor.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


//@Configuration
//@EnableJpaAuditing
//public class AuditConfiguration {
//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        return new SpringSecurityAuditorAware();
//    }
//}
