package com.casdoor.demo.controller;



import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.casbin.casdoor.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ApplicationController {


    @Resource
    private AuthService casdoorAuthService;
    @Resource
    HttpSession session;
    @Resource
    HttpServletResponse response;

    @RequestMapping("toLogin")
    public String toLogin() {
        return "redirect:" + casdoorAuthService.getSigninUrl("http://localhost:8080/login");
    }
}
