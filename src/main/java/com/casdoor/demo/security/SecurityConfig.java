package com.casdoor.demo.security;

import ch.qos.logback.classic.spi.LoggerContextAwareBase;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/callback").permitAll()
                                .anyRequest().authenticated()
                )
                .cors(withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login", "/callback")
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwtConfigurer -> jwtAuthenticationConverter())
                );

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Adjust as needed for your environment
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        log.debug("*******inside jwtAuthenticationConverter method*******");

        //   A JwtGrantedAuthoritiesConverter is created, which specifies how to extract authorities from JWT claims.
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // The authority claim name is set to "roles"
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        //the authority prefix is set to "ROLE_". This means that authorities listed in the "roles" claim of the JWT
        // will be converted into GrantedAuthority objects with "ROLE_" prefix, which is the typical convention used
        // in Spring Security for role-based access control.
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // The JwtGrantedAuthoritiesConverter is set as the converter for the JwtAuthenticationConverter.
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}

