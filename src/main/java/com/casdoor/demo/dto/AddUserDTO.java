package com.casdoor.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {

    public String firstName;
    public String lastName;
    @NotNull(value = "username can't be null")
    public String username;
    @NotNull(value = "password can't be null")
    public String password;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    public String email;
    @NotNull(value = "mobile number can't be null")
    public String mobileNumber;
    public String address;
    public String region;
    public String language;
    public String gender;
    public String status = "active";


    @NotNull(value = "role can't be null")
    public String role;

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public void setMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            throw new IllegalArgumentException("Mobile number cannot be null");
        }
        if (mobileNumber.contains("-")) {
            String[] delimited = mobileNumber.split("-");
            String countryCode = delimited[0];
            String number = delimited[1];
            if (countryCode.equals("+971")) {
                if (number.length() != 9) {
                    //error
                    throw new IllegalArgumentException("Invalid mobile number.Mobile number should contain 9 digits");
                }
            } else if (countryCode.equals("+91")) {
                if (number.length() != 10) {
                    throw new IllegalArgumentException("Invalid mobile number.Mobile number should contain 10 digits");
                }
            } else {
                throw new IllegalArgumentException("Invalid country code");
            }
        } else {
            throw new IllegalArgumentException("Please provide a valid mobile number with country code delimited by '-' ");
        }
        this.mobileNumber = mobileNumber;
    }
}
