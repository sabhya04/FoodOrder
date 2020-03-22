package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Payment repository class
 */
@Repository
public class PaymentsRepository {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * getPaymentMediums
     * @return
     */
    public List<PaymentEntity> getPaymentMediums() {

        List<PaymentEntity> paymentMediumList = null;
        try {
            TypedQuery<PaymentEntity> query = entityManager.createNamedQuery("getAllPaymentMethods", PaymentEntity.class);
            paymentMediumList = query.getResultList();
        } catch (NoResultException e) {

        }
        return paymentMediumList;
    }

    /**
     * getPayment
     * @param uuid
     * @return
     */
    public PaymentEntity getPayment(String uuid) {
        PaymentEntity paymentEntity = null;
        try {

            TypedQuery<PaymentEntity> query = entityManager.createNamedQuery("getPaymentMethodById", PaymentEntity.class);
            query.setParameter("uuid", uuid);
            paymentEntity = query.getSingleResult();
        } catch (NoResultException e) {


        }
        return paymentEntity;
    }
}
