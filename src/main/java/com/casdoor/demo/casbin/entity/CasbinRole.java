package com.casdoor.demo.casbin.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "casbin_role")
public class CasbinRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String roleName;

    private String describe;

    private String status;

    private Long createTime;

    private Long updateTime;


}
