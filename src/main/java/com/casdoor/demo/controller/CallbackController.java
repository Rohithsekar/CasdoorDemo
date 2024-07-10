package com.casdoor.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CallbackController {

    @GetMapping("/callback")
    public ModelAndView callbackAfterSuccessfulLogin(){
        return new ModelAndView("successLogin");

    }
}
