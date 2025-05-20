package com.evaluation.register.register_api.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "phones",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "number" }) })
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User userId;

    @Column(name = "number")
    private String number;

    @Column(name = "citycode")
    private String citycode;

    @Column(name = "countrycode")
    private String countrycode;
}
