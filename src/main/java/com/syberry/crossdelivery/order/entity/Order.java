package com.syberry.crossdelivery.order.entity;

import com.syberry.crossdelivery.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @NotNull
    private User owner;
    @ManyToOne
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    private User performer;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;
    @NotNull
    @Column(length = 80)
    private String title;
    @Column(length = 500)
    private String description;
    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private double price;
    @NotNull
    @Column(length = 50)
    private String departureCountry;
    @NotNull
    @Column(length = 100)
    private String departureLocation;
    @NotNull
    @Column(length = 50)
    private String destinationCountry;
    @NotNull
    @Column(length = 100)
    private String destinationLocation;
    private boolean blocked = false;
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();
}
