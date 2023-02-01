package com.syberry.crossdelivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto extends OrderShortViewDto {

    private Long ownerId;
    @NotBlank
    private String description;
    private boolean blocked;
}
