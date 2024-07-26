package com.casdoor.demo.casbin.controller;


import com.casdoor.demo.casbin.entity.CasbinResource;
import com.casdoor.demo.casbin.repository.ResourceRespository;
import com.casdoor.demo.constants.Constants;
import com.casdoor.demo.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/resource/v1")
public class ResourceController {


    private final ResourceRespository resourceRespository;

    public ResourceController(ResourceRespository resourceRespository) {
        this.resourceRespository = resourceRespository;
    }

    @GetMapping
    public ResponseEntity<APIResponse> getResource(@RequestParam @NotNull String resourceName) {
        return ResponseEntity.ok().body(new APIResponse(Constants.SUCCESS, "fetched", "hello"));
    }


    @PostMapping
    public ResponseEntity<APIResponse> create(@RequestBody final CasbinResource casbinResource) {

        CasbinResource saved =  resourceRespository.save(casbinResource);
        return new ResponseEntity<>(new APIResponse(Constants.SUCCESS, "resource created successfully", saved), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<APIResponse> update(@Valid @RequestBody CasbinResource casbinResource) {

        return null;
    }


}
