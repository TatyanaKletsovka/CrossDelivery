package com.syberry.crossdelivery.performrequest.service.impl;

import com.syberry.crossdelivery.exception.AccessException;
import com.syberry.crossdelivery.order.entity.Order;
import com.syberry.crossdelivery.order.entity.Status;
import com.syberry.crossdelivery.order.repository.OrderRepository;
import com.syberry.crossdelivery.performrequest.converter.PerformRequestConverter;
import com.syberry.crossdelivery.performrequest.dto.PerformRequestDto;
import com.syberry.crossdelivery.performrequest.dto.PerformRequestFilterDto;
import com.syberry.crossdelivery.performrequest.entity.PerformRequest;
import com.syberry.crossdelivery.performrequest.repository.PerformRequestRepository;
import com.syberry.crossdelivery.performrequest.service.PerformRequestService;
import com.syberry.crossdelivery.performrequest.service.specification.PerformRequestSpecification;
import com.syberry.crossdelivery.user.entity.User;
import com.syberry.crossdelivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.syberry.crossdelivery.authorization.util.SecurityContextUtil.getUserDetails;

@Service
@RequiredArgsConstructor
public class PerformRequestServiceImpl implements PerformRequestService {

    private final PerformRequestRepository requestRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PerformRequestConverter requestConverter;
    private final PerformRequestSpecification specification;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<PerformRequestDto> getAuthorisedUserOrdersRequests(
        PerformRequestFilterDto filter, Pageable pageable) {
        return requestRepository.findAll(
            specification.buildGetPersonalOrdersRequestsSpecification(filter), pageable)
            .map(requestConverter::convertToDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<PerformRequestDto> getAuthorisedUserPerformRequests(
        PerformRequestFilterDto filter, Pageable pageable) {
        return requestRepository.findAll(
            specification.buildGetPersonalRequestsSpecification(filter), pageable)
            .map(requestConverter::convertToDto);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PerformRequestDto getPerformRequestById(Long id) {
        PerformRequest request = requestRepository.findByIdIfExists(id);
        validateIfRelatedPerformRequest(request);
        return requestConverter.convertToDto(request);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PerformRequestDto createPerformRequestByOrderId(Long id) {
        Optional<PerformRequest> optional = requestRepository.findByOrderIdAndPerformerId(
            id, getUserDetails().getId());
        if (optional.isPresent()) {
            throw new AccessException("The request is already created for this order");
        }
        Order order = orderRepository.findByIdAndBlockedFalseIfExists(id);
        validateIfRequestToOwnedOrder(order);
        validateIfOrderWithPerformer(order);
        User performer = userRepository.findByIdIfExistsAndBlockedFalse(getUserDetails().getId());
        PerformRequest request = new PerformRequest();
        request.setOrder(order);
        request.setPerformer(performer);
        if (order.getStatus() == Status.NEW) {
            order.setStatus(Status.WITH_REQUESTS);
        }
        return requestConverter.convertToDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public void deletePerformRequestById(Long id) {
        PerformRequest request = requestRepository.findByIdIfExists(id);
        validateIfOwnedRequest(request);
        validateIfCanBeDeletedByPerformer(request.getOrder());
        validateIfCanBeDeletedByApproved(request);
        requestRepository.deleteById(id);
        changeStatusIfLastRequestInOrder(request.getOrder());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PerformRequestDto approvePerformRequestById(Long id) {
        PerformRequest request = requestRepository.findByIdIfExists(id);
        Order order = request.getOrder();
        validateIfOwnedOrder(order);
        validateIfOrderWithPerformer(order);
        request.setApproved(true);
        order.setStatus(Status.WITH_PERFORMER);
        order.setPerformer(request.getPerformer());
        return requestConverter.convertToDto(request);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public PerformRequestDto rejectPerformRequestById(Long id) {
        PerformRequest request = requestRepository.findByIdIfExists(id);
        validateIfOwnedOrder(request.getOrder());
        deletePerformerFromOrderIfPossible(request);
        request.setApproved(false);
        return requestConverter.convertToDto(request);
    }


    private void validateIfRequestToOwnedOrder(Order order) {
        if (order.getOwner().getId().equals(getUserDetails().getId())) {
            throw new AccessException("The user can't add requests to his own orders");
        }
    }

    private void validateIfOrderWithPerformer(Order order) {
        if (order.getPerformer() != null) {
            throw new AccessException(
                "The requests can't be added/approved to the order with performer");
        }
    }

    private void validateIfOwnedRequest(PerformRequest request) {
        if (!request.getPerformer().getId().equals(getUserDetails().getId())) {
            throw new AccessException("Only own request can be deleted");
        }
    }


    private void validateIfCanBeDeletedByPerformer(Order order) {
        if (order.getPerformer() != null) {
            throw new AccessException(
                "The request can't be deleted from the order with performer");
        }
    }
    private void validateIfCanBeDeletedByApproved(PerformRequest request) {
        if (request.getApproved() != null && !request.getApproved()) {
            throw new AccessException("The rejected request can't be deleted from the order");
        }
    }

    private void validateIfOwnedOrder(Order order) {
        if (!order.getOwner().getId().equals(getUserDetails().getId())) {
            throw new AccessException(
                "The user can approve/reject requests only for his own orders");
        }
    }

    private void validateIfRelatedPerformRequest(PerformRequest request) {
        Long id = getUserDetails().getId();
        if (!request.getPerformer().getId().equals(id)
            && !request.getOrder().getOwner().getId().equals(id)) {
            throw new AccessException("Not own perform requests can't be viewed");
        }
    }

    private void deletePerformerFromOrderIfPossible(PerformRequest request) {
        Order order = request.getOrder();
        if (!order.getStatus().equals(Status.NEW)
            && !order.getStatus().equals(Status.WITH_REQUESTS)
                && !order.getStatus().equals(Status.WITH_PERFORMER)) {
            throw new AccessException("Perform request can't be deleted from order in process");
        }
        if (order.getPerformer() != null
            && order.getPerformer().getId().equals(request.getPerformer().getId())) {
            order.setPerformer(null);
            order.setStatus(Status.WITH_REQUESTS);
        }
    }

    private void changeStatusIfLastRequestInOrder(Order order) {
        List<PerformRequest> requests = requestRepository.findByOrderId(order.getId());
        if (requests.size() == 0) {
            order.setStatus(Status.NEW);
        }
    }
}
