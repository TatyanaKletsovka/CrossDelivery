package com.syberry.crossdelivery.performrequest.service;

import com.syberry.crossdelivery.performrequest.dto.PerformRequestDto;
import com.syberry.crossdelivery.performrequest.dto.PerformRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformRequestService {
    
    Page<PerformRequestDto> getAuthorisedUserOrdersRequests(PerformRequestFilterDto filter, Pageable pageable);

    Page<PerformRequestDto> getAuthorisedUserPerformRequests(PerformRequestFilterDto filter, Pageable pageable);

    PerformRequestDto getPerformRequestById(Long id);


    PerformRequestDto createPerformRequestByOrderId(Long id);

    void deletePerformRequestById(Long id);

    PerformRequestDto approvePerformRequestById(Long id);

    PerformRequestDto rejectPerformRequestById(Long id);
}
