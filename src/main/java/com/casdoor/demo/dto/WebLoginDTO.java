package com.casdoor.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebLoginDTO {

    @NotNull(message = "username can't be null")
    private String username;
    @NotNull(message = "password can't be null")
    private String password;
    private String firebaseToken;
    private String mobileNumber;
}
