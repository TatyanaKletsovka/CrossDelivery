package com.syberry.crossdelivery.performrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformRequestFilterDto {

    private Long id;
    private Long orderId;
    private Long ownerId;
    private Long performerId;
    private Boolean approved;
    private LocalDate createdAtStart = LocalDate.of(2000, 1, 1);
    private LocalDate createdAtEnd = LocalDate.of(3000, 1, 1);
}
