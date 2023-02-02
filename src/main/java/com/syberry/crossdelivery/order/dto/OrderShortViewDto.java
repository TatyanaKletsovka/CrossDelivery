package com.syberry.crossdelivery.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderShortViewDto {

    private Long id;
    @NotBlank
    private String title;
    @NotNull
    @Positive
    private double price;
    @NotBlank
    private String departureCountry;
    @NotBlank
    private String departureLocation;
    @NotBlank
    private String destinationCountry;
    @NotBlank
    private String destinationLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
}
