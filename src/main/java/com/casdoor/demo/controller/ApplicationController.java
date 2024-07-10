package com.casdoor.demo.controller;


import com.casdoor.demo.dto.RestData;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.casbin.casdoor.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplicationController {


    @Resource
    private AuthService casdoorAuthService;
    @Resource
    HttpSession session;
    @Resource
    HttpServletResponse response;

    @ResponseBody
    @SneakyThrows
    @RequestMapping("/goSignin")
    public RestData goLogin(@RequestParam String redirect,
                            String callback,
                            @RequestParam(required = false) boolean go) {
        this.session.setAttribute("callback", callback);
        String url = casdoorAuthService.getSigninUrl(redirect);
        if (go) {
            response.sendRedirect(url);
            return null;
        }
        return RestData.builder().data(url).build();
    }

}
