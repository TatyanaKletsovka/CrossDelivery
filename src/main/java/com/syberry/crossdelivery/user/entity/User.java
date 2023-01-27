package com.syberry.crossdelivery.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(length = 50, unique = true)
    private String username;
    @NotNull
    @Column(length = 50)
    private String firstName;
    @NotNull
    @Column(length = 50)
    private String lastName;
    @NotNull
    @Email
    @Column(length = 50, unique = true)
    private String email;
    @NotNull
    private String password;
    @NotNull
    @Pattern(regexp = "^\\d{10}$", message = "The phone number must consist of 10 digits")
    private String phoneNumber;
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;
    private boolean isBlocked = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime disabledAt;
}
