package com.syberry.crossdelivery.performrequest.controller;

import com.syberry.crossdelivery.performrequest.dto.PerformRequestDto;
import com.syberry.crossdelivery.performrequest.dto.PerformRequestFilterDto;
import com.syberry.crossdelivery.performrequest.service.PerformRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/perform-requests")
public class PerformRequestController {

    private final PerformRequestService requestService;

    @GetMapping("/requested")
    public Page<PerformRequestDto> getOrderPerformRequestsByOrderId(
        PerformRequestFilterDto filter, Pageable pageable) {
        log.info("GET-request: getting all perform requests of authorised user orders");
        return requestService.getAuthorisedUserOrdersRequests(filter, pageable);
    }

    @GetMapping("/created")
    public Page<PerformRequestDto> getAuthorisedUserPerformRequests(
        PerformRequestFilterDto filter, Pageable pageable) {
        log.info("GET-request: getting all perform requests of authorised user");
        return requestService.getAuthorisedUserPerformRequests(filter, pageable);
    }

    @GetMapping("/{id}")
    public PerformRequestDto getPerformRequestById(@PathVariable("id") Long id) {
        log.info("GET-request: getting perform request with id: {}", id);
        return requestService.getPerformRequestById(id);
    }


    @PostMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PerformRequestDto createPerformRequestByOrderId(@PathVariable("id") Long id){
        log.info("POST-request: creating perform request for the order with id: {}", id);
        return requestService.createPerformRequestByOrderId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerformRequestById(@PathVariable("id") Long id) {
        log.info("DELETE-request: deleting perform request with id: {}", id);
        requestService.deletePerformRequestById(id);
    }

    @PutMapping("/{id}/approve")
    public PerformRequestDto approvePerformRequestById(@PathVariable("id") Long id) {
        log.info("PUT-request: approving perform request with id: {}", id);
        return requestService.approvePerformRequestById(id);
    }

    @PutMapping("/{id}/reject")
    public PerformRequestDto rejectPerformRequestById(@PathVariable("id") Long id) {
        log.info("PUT-request: rejecting perform request with id: {}", id);
        return requestService.rejectPerformRequestById(id);
    }
}
