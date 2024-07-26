package com.casdoor.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.entity.Role;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.RoleService;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final AuthService casdoorAuthService;

    private final RoleService roleService;
    private final Enforcer enforcer;

    private final AntPathMatcher antPathMatcher;

    private RequestMeta requestMeta;

    public JwtTokenFilter(AuthService casdoorAuthService,
                          RoleService roleService,
                          Enforcer enforcer,
                          AntPathMatcher antPathMatcher,
                          RequestMeta requestMeta) {
        this.casdoorAuthService = casdoorAuthService;
        this.roleService = roleService;
        this.enforcer = enforcer;
        this.antPathMatcher = antPathMatcher;
        this.requestMeta = requestMeta;
    }

    private final List<String> whiteList = Arrays.asList(
            "/api/user/v1/login");

    private final List<String> customerAdminResources = Arrays.asList("/api/v1/customerAdmin/**");


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        log.info("incoming request URL is {}", request.getRequestURL());

        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                User user = casdoorAuthService.parseJwtToken(token);

//                List<Role> roles = user.roles;
//
//                List<String> userRoles = roles.stream().map(r -> r.name).collect(Collectors.toUnmodifiableList());
                log.info("username is {}", user.name);
                String userRole = "";

                boolean bypassFilter = whiteList.stream().anyMatch(whiteListedEndpoint -> antPathMatcher.match(whiteListedEndpoint, requestURI));

                if (!bypassFilter) {

                    log.info("checking access for user....");

                    boolean customerAdminResource = customerAdminResources.stream().anyMatch(resource -> antPathMatcher.match(resource, requestURI));

                    if (customerAdminResource) {

                        Role role = roleService.getRole("customerAdmin");

                        List<String> usernames = Arrays.asList(role.users);

                        if (usernames.contains(user.name)) {

                            userRole = "customerAdmin";
                            log.info("access granted");
                        }


                    }

                    boolean allow = hasAccess(userRole, requestURI, requestMethod);

                    if(!allow){
                        log.info(" don't have access");
                    }else {
                        log.info(" have access");
                    }


                } else {
                    log.info("bypassed security filter....");
                }


                requestMeta.setUsername(user.name);
                requestMeta.setEntityName("yaantrac");


                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

    private boolean hasAccess(String name, String requestURI, String requestMethod) {

        if (enforcer.enforce(name, requestURI, requestMethod) == true) {
            return true;
        } else {
            log.error(" user: {} dont have enough permissions to access resource {}", name, requestURI);
            // deny the request, show an error
            throw new AccessDeniedException("Don't have permissions to access the resource");
        }
    }


}

