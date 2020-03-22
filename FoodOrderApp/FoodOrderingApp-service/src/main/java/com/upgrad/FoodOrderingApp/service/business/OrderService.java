package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.OrderItemRepository;
import com.upgrad.FoodOrderingApp.service.dao.OrderRepository;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business service class, for order placing functionalities
 */
@Service
@Transactional
public class OrderService {


    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * @param couponName
     * @return
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponName(String couponName) throws CouponNotFoundException {
        CouponEntity couponEntity = orderRepository.getCouponByCouponName(couponName);
        if (couponEntity == null) {

            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        return couponEntity;

    }

    /**
     * @param id
     * @return
     * @throws CouponNotFoundException
     */
    public CouponEntity getCouponByCouponId(String id) throws CouponNotFoundException {
        CouponEntity couponEntity = orderRepository.getCouponByCouponId(id);
        if (couponEntity == null) {

            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }
        return couponEntity;


    }

    /**
     * @param ordersEntity
     * @return
     */
    public OrderEntity saveOrder(OrderEntity ordersEntity) {
        return orderRepository.savePlacedOrder(ordersEntity);

    }

    /**
     * @param o
     * @return
     */

    public OrderItemEntity saveOrderItem(OrderItemEntity o) {

        return orderItemRepository.saveOrderItem(o);
    }

    /**
     * @param uuid
     * @return
     */
    public List<OrderEntity> getOrdersByCustomers(String uuid) {

        return orderRepository.getAllPastOrdersOfCustomer(uuid);

    }
}
