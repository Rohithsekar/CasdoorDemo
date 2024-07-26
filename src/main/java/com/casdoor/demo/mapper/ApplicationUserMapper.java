package com.casdoor.demo.mapper;

import com.casdoor.demo.dto.AddUserDTO;
import com.casdoor.demo.entity.ApplicationUser;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(ApplicationUserDecorator.class)
public interface ApplicationUserMapper {

    @Mapping(source = "username", target = "casdoorUsername")
    @Mapping(source = "email", target = "casdoorEmail")
    @Mapping(source = "mobileNumber", target = "casdoorMobileNumber")
    ApplicationUser fromDto(AddUserDTO addUserDto);
}
