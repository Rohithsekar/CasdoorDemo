package com.casdoor.demo.mapper;

import com.casdoor.demo.dto.AddUserDTO;
import com.casdoor.demo.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public abstract class ApplicationUserDecorator implements ApplicationUserMapper{

    @Autowired
    @Qualifier("delegate")
    private ApplicationUserMapper delegate;
    @Autowired
    protected PasswordEncoder passwordEncoder;

//    @Mapping(source = "username", target = "casdoorUsername")
//    @Mapping(source = "email", target = "casdoorEmail")
//    @Mapping(source = "mobileNumber", target = "casdoorMobileNumber")
//    @Mapping(source = "status", target = "status")
//    @Mapping(target = "createdTimestamp", expression = "java(this.setTime(addUserDto))")
//    @Mapping(target = "createdBy", constant = "1")
//    @Mapping(target = "casdoorPassword", expression = "java(this.setHashedPassword(addUserDto.getPassword()))") // Access password via custom method
//    public abstract ApplicationUser fromDto(AddUserDTO addUserDto);


    @Override
    public ApplicationUser fromDto(AddUserDTO addUserDto) {
        ApplicationUser applicationUser = delegate.fromDto(addUserDto);

        applicationUser.setCreatedDate(new Date());
        applicationUser.setCreatedBy("Super Admin");
        applicationUser.setCasdoorPassword(passwordEncoder.encode(addUserDto.getPassword()));

        return applicationUser;
    }


}