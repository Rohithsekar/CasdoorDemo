package com.casdoor.demo.casbin.repository;

import com.casdoor.demo.casbin.entity.CasbinResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRespository extends JpaRepository<CasbinResource, Integer> {


}
