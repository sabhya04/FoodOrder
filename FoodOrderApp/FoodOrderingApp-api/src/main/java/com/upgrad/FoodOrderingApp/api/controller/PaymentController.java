package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.business.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * This class handles request to retrieve payment related information before placing of order in
 * checkout page
 */
@RestController
@CrossOrigin(value = "*", maxAge = 1L)
@RequestMapping("/payment")
public class PaymentController {
  @Autowired private PaymentService paymentsService;
  @Autowired private CustomerService customerService;

  /**
   * Retrieves list of payment mediums from static table.
   * @return PaymentListResponse
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaymentListResponse> getPaymentMethods() {
    List<PaymentEntity> paymentEntityList = paymentsService.getAllPaymentMethods();

    List<PaymentResponse> paymentResponseList = new LinkedList<>();
    for (PaymentEntity paymentEntity : paymentEntityList) {
      PaymentResponse paymentResponse = new PaymentResponse();
      paymentResponse.setPaymentName(paymentEntity.getPaymentName());
      UUID uuid = UUID.fromString(paymentEntity.getUuid());
      paymentResponse.setId(uuid);
      paymentResponseList.add(paymentResponse);
    }
    PaymentListResponse paymentListResponse = new PaymentListResponse();
    paymentListResponse.setPaymentMethods(paymentResponseList);
    return new ResponseEntity<>(paymentListResponse, HttpStatus.OK);
  }
}