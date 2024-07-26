package com.casdoor.demo.controller;

import com.casdoor.demo.constants.Constants;
import com.casdoor.demo.dto.APIResponse;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/customerAdmin")
public class CustomerAdminController {

    @GetMapping("/hello")
    public ResponseEntity<APIResponse> hello(){
        return ResponseEntity.ok().body(new APIResponse(Constants.SUCCESS, "Hello world", new ArrayList<>()));
    }
}
