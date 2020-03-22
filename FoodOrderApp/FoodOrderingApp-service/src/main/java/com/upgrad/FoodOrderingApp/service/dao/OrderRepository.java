package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Order repository class
 */
@Repository
public class OrderRepository {
    @PersistenceContext
    EntityManager entityManager;

    /**
     * getCouponByCouponName
     * @param couponName
     * @return
     */
    public CouponEntity getCouponByCouponName(String couponName) {
        CouponEntity couponEntity = null;

        try {

            TypedQuery<CouponEntity> query = entityManager.createNamedQuery("findCouponByCouponName", CouponEntity.class);
            query.setParameter("couponName", couponName);
            couponEntity = query.getSingleResult();
        } catch (NoResultException e) {

        }
        return couponEntity;
    }

    /**
     * getCouponByCouponId
     * @param uuid
     * @return
     */
    public CouponEntity getCouponByCouponId(String uuid) {
        CouponEntity couponEntity = null;

        try {

            TypedQuery<CouponEntity> query = entityManager.createNamedQuery("findCouponByCouponId", CouponEntity.class);
            query.setParameter("uuid", uuid);
            couponEntity = query.getSingleResult();
        } catch (NoResultException e) {

        }
        return couponEntity;
    }

    /**
     * @param transaction
     * @return
     */
    public OrderEntity savePlacedOrder(OrderEntity transaction) {

        entityManager.persist(transaction);
        return transaction;
    }

    /**
     * @param customerId
     * @return
     */
    public List<OrderEntity> getAllPastOrdersOfCustomer(String customerId) {

        List<OrderEntity> orders = null;
        try {
            TypedQuery<OrderEntity> query = entityManager.createNamedQuery("findOrdersByCustomerId", OrderEntity.class);

            query.setParameter("customerId", customerId);
            orders = query.getResultList();
        } catch (NoResultException e) {


        }
        return orders;
    }

}
