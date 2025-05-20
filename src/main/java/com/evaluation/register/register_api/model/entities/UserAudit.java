package com.evaluation.register.register_api.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "users-audit",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "token" }) })
public class UserAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created")
    private Date created;

    @Column(name = "modified")
    private Date modified;

    @Column(name = "last_login")
    private Date lastLogin;
}
