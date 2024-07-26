package com.casdoor.demo.casbin.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;



@Table(name = "resource")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasbinResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String resourceName;

    private String resourcePath;

    private Integer parentId;

    private Integer status;

    private Long createTime;

    private Long updateTime;

    private String permission;


    public CasbinResource(String resourceName, String resourcePath,
                          Integer parentId, Integer status,
                          String permission) {

        this.resourceName = resourceName;
        this.resourcePath = resourcePath;
        this.parentId = parentId;
        this.status = status;
        this.createTime = Instant.now().getEpochSecond();
        this.permission = permission;
    }
}
