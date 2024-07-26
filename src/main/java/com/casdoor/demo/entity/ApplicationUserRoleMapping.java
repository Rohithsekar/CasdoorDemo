package com.casdoor.demo.entity;

import com.casdoor.demo.casbin.entity.CasbinRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "user_role_mapping")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApplicationUserRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private ApplicationUser applicationUser;
    private CasbinRole role;
    private String casdoorUserUniqueId;
    private Long createdTime;
    private Long updatedTime;

}
