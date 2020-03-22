package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.PaymentsRepository;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business service class for functionalities related to payment entity
 */
@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentsRepository paymentsRepository;

    /**
     * @return
     */
    public List<PaymentEntity> getAllPaymentMethods() {

        return paymentsRepository.getPaymentMediums();
    }

    /**
     * @param id
     * @return
     * @throws PaymentMethodNotFoundException
     */
    public PaymentEntity getPaymentByUUID(String id) throws PaymentMethodNotFoundException {


        PaymentEntity paymentEntity = paymentsRepository.getPayment(id);

        if (paymentEntity == null) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        }
        return paymentEntity;
    }
}
