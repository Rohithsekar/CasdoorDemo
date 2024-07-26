package com.casdoor.demo.dto;


import com.casdoor.demo.constants.AuthorizationConstants;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {

    @NotNull(message = "resouce type cannot be null")
    private String resourceType;

    @NotEmpty(message = "resources cannot be null") // Ensure resources list is not empty
    private Set<String> resources;

    @NotNull(message = "actions cannot be null")
    private Set<String> actions;

    @NotNull(message = "effect cannot be null")
    private String effect;


    public void setResourceType(String resourceType) {

        if(Arrays.asList(AuthorizationConstants.ALL_RESOURCES).contains(resourceType)){
            this.resourceType = resourceType;
        }else{
            throw new IllegalArgumentException("Resource types should be one of the allowed: " + Arrays.toString(AuthorizationConstants.RESOURCE_TYPES));
        }

    }

    public void setResources(Set<String> resources) {

        if(isValidResources(resources)){
            this.resources = resources;
        }else{
            throw new IllegalArgumentException("Resources should be one of the allowed: " + Arrays.toString(AuthorizationConstants.ALL_RESOURCES));
        }

    }

    public void setActions(Set<String> actions) {

        if(isValidActions(actions)){
            this.actions =actions;
        }else {
            throw new IllegalArgumentException("Actions should be one of the allowed: " + Arrays.toString(AuthorizationConstants.ALL_ACTIONS));
        }
    }


    public void setEffect(String effect) {
        if(effect.equalsIgnoreCase("allow") || effect.equalsIgnoreCase("deny")){
            this.effect = effect;
        }else{
            throw new IllegalArgumentException("Effect should be either allow or deny");
        }

    }

    public boolean isValidResources(Set<String> userResourcesSet) {
        // Use a Set for efficient membership checking
        Set<String> allResourcesSet = new HashSet<>(Arrays.asList(AuthorizationConstants.ALL_RESOURCES));
        return !userResourcesSet.isEmpty() || allResourcesSet.containsAll(userResourcesSet);
    }

    public boolean isValidActions(Set<String> actionsSet) {
        // Use a Set for efficient membership checking
        Set<String> allActionsSet = new HashSet<>(Arrays.asList(AuthorizationConstants.ALL_ACTIONS));
        return !actionsSet.isEmpty() || allActionsSet.containsAll(actionsSet);
    }
}