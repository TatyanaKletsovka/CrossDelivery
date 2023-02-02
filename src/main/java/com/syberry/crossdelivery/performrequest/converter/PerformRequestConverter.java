package com.syberry.crossdelivery.performrequest.converter;

import com.syberry.crossdelivery.performrequest.dto.PerformRequestDto;
import com.syberry.crossdelivery.performrequest.entity.PerformRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PerformRequestConverter {

    public PerformRequestDto convertToDto(PerformRequest performRequest) {
        return PerformRequestDto.builder()
                .id(performRequest.getId())
                .orderId(performRequest.getOrder().getId())
                .performerId(performRequest.getPerformer().getId())
                .approved(performRequest.getApproved())
                .createdAt(performRequest.getCreatedAt())
                .build();
    }
}
