package com.syberry.crossdelivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterDto {

    private String status;
    private String title = "";
    private double priceStart = 0;
    private double priceEnd = 1000000;
    private String departureCountry = "";
    private String departureLocation = "";
    private String destinationCountry = "";
    private String destinationLocation = "";
    private Boolean blocked;
    private LocalDate createdAtStart = LocalDate.of(2000, 1, 1);
    private LocalDate createdAtEnd = LocalDate.of(3000, 1, 1);
}
