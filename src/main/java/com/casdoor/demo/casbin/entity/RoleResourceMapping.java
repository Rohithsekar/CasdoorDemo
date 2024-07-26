package com.casdoor.demo.casbin.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "casbin_role")
public class RoleResourceMapping implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private CasbinRole role;
    private CasbinResource resource;
    private Long createdTime;
    private Long updatedTime;


}
