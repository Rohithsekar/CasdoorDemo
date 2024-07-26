package com.casdoor.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Getter
@Setter
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ApplicationUser extends AuditMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    private String status;

    @Column(name = "casdoor_username", unique = true, updatable = false)
    private String casdoorUsername;

    @Column(name = "casdoor_password")
    private String casdoorPassword;

    @Column(name = "casdoor_email",unique = true)
    private String casdoorEmail;

    @Column(name = "casdoor_mobile_number", unique = true)
    private String casdoorMobileNumber;




}
