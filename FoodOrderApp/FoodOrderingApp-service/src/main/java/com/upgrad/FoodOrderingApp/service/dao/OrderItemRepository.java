package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * OrderItem repository class
 */
@Repository
public class OrderItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * saveOrderItem
     * @param o
     * @return
     */
    public OrderItemEntity saveOrderItem(OrderItemEntity o) {
        try {

            entityManager.persist(o);
        } catch (Exception e) {

        }

        return o;
    }

}


