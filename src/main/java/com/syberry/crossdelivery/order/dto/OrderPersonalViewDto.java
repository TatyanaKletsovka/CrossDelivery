package com.syberry.crossdelivery.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.syberry.crossdelivery.order.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPersonalViewDto {

    private Long id;
    private Long ownerId;
    private Long performerId;
    private Status status;
    private String title;
    private double price;
    private boolean blocked;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
}
