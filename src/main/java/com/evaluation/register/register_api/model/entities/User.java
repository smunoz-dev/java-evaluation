package com.evaluation.register.register_api.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "users",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "phones")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Phone> phones;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active")
    private Boolean isActive;

    @CreationTimestamp
    @Column(updatable = false, name = "created")
    private Date created;

    @UpdateTimestamp
    @Column(name = "modified")
    private Date modified;

    @Column(name = "last_login")
    private Date lastLogin;
}
