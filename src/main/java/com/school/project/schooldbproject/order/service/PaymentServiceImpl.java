package com.school.project.schooldbproject.order.service;

import com.school.project.schooldbproject.branch.entity.Branch;
import com.school.project.schooldbproject.branch.entity.Inventory;
import com.school.project.schooldbproject.branch.repository.InventoryRepository;
import com.school.project.schooldbproject.global.error.exception.BusinessException;
import com.school.project.schooldbproject.global.error.exception.EntityNotFoundException;
import com.school.project.schooldbproject.global.error.exception.ErrorCode;
import com.school.project.schooldbproject.order.dto.CreateOrderDetailDto;
import com.school.project.schooldbproject.order.dto.CreatePaymentDto;
import com.school.project.schooldbproject.order.dto.PaymentDto;
import com.school.project.schooldbproject.order.entity.OrderDetail;
import com.school.project.schooldbproject.order.entity.Payment;
import com.school.project.schooldbproject.order.repository.OrderDetailRepository;
import com.school.project.schooldbproject.order.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final InventoryRepository inventoryRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Autowired
    public PaymentServiceImpl(InventoryRepository inventoryRepository, PaymentRepository paymentRepository, OrderDetailRepository orderDetailRepository) {
        this.inventoryRepository = inventoryRepository;
        this.paymentRepository = paymentRepository;
        this.orderDetailRepository = orderDetailRepository;
    }


    @Transactional
    @Override
    public PaymentDto.Response createPayment(CreatePaymentDto createPaymentDto) {
        List<Inventory> inventories = inventoryRepository.findByBranchId(createPaymentDto.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("?????? ????????? ID??? ????????? ?????? ??? ????????????"));

        List<CreateOrderDetailDto> orderDetailsDtos = createPaymentDto.getOrderDetails();
        List<OrderDetail> orderDetails = new ArrayList<>();

        List<Inventory> willUpdateInventories = orderDetailsDtos.stream()
                .map(orderItemDto -> {
                    Long orderCatalogueId = orderItemDto.getCatalogueId();
                    Inventory foundInventoryItem = inventories.stream()
                            .filter(inventoryItem -> inventoryItem.getCatalogue().getId().equals(orderCatalogueId))
                            .findAny()
                            .orElseThrow(() -> new EntityNotFoundException("????????? ?????? ID??? ????????? ?????? ??? ????????????. ?????? ID: " + orderCatalogueId));

                    Long orderQuantity = orderItemDto.getQuantity();
                    if (foundInventoryItem.getStock() < orderQuantity) {
                        throw new BusinessException("????????? ????????? ????????? ?????? ???????????? ????????????. ?????? ??????: " + foundInventoryItem.getStock(), ErrorCode.INVALID_INPUT_VALUE);
                    }

                    /** ????????? foundInventoryItem ?????? ?????? ????????? */
                    OrderDetail orderItem = OrderDetail.createOrderItem(foundInventoryItem, orderQuantity);
                    orderDetailRepository.save(orderItem);
                    orderDetails.add(orderItem);

                    return foundInventoryItem;

                }).collect(Collectors.toList());

        Branch branch = inventories.get(0).getBranch();
        Payment payment = Payment.createPayment(branch, orderDetails);

        inventoryRepository.updateInventories(willUpdateInventories);
        paymentRepository.save(payment);

        return new PaymentDto.Response(payment);
    }

    @Override
    public List<PaymentDto.Response> findPaymentsByBranchId(Long branchId) {
        List<Payment> payments = paymentRepository.findAllByBranchId(branchId)
                .orElseThrow(() -> new EntityNotFoundException("????????? ID??? ?????? ????????? ?????? ??? ????????????. ????????? ????????? ID: " + branchId));

        return payments.stream().map(PaymentDto.Response::new).collect(Collectors.toList());
    }
}
