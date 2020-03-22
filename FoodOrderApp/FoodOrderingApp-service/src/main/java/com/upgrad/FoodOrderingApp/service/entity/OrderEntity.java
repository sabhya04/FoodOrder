package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Order entity class
 * */

@Entity
@Table(name = "orders")
@NamedQueries({
  @NamedQuery(
      name = "findOrdersByCustomerId",
      query = "select o from OrderEntity o where o.customer.uuid =:customerId"),
  @NamedQuery(
      name = "findOrderByAddressId",
      query = "Select o from OrderEntity o where o.address.Id = :addressId")
})

public class OrderEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer Id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "bill")
  @NotNull
  private double bill;

  @OneToOne
  @JoinColumn(name = "coupon_id", referencedColumnName = "id")
  private CouponEntity couponEntityId;

  @Column(name = "discount")
  private double discount;

  @Column(name = "date")
  @NotNull
  private Date date;

  @OneToOne
  @JoinColumn(name = "payment_id", referencedColumnName = "id")
  private PaymentEntity paymentEntityId;

  @OneToOne(cascade = CascadeType.REMOVE)
  @NotNull
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private CustomerEntity customer;

  @OneToOne
  @NotNull
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @OneToOne
  @NotNull
  @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
  private RestaurantEntity restaurantId;

  // Bi-Directional Mapping
  @OneToMany(mappedBy = "orderId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<OrderItemEntity> orderItems;

  public OrderEntity() {}

  public OrderEntity(
      @NotNull @Size(max = 200) String uuid,
      @NotNull double bill,
      CouponEntity couponEntityId,
      double discount,
      @NotNull Date date,
      PaymentEntity paymentEntityId,
      @NotNull CustomerEntity customer,
      @NotNull AddressEntity address,
      @NotNull RestaurantEntity restaurantId) {
    this.uuid = uuid;
    this.bill = bill;
    this.couponEntityId = couponEntityId;
    this.discount = discount;
    this.date = date;
    this.paymentEntityId = paymentEntityId;
    this.customer = customer;
    this.address = address;
    this.restaurantId = restaurantId;
  }

  public List<OrderItemEntity> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItemEntity> orderItems) {
    this.orderItems = orderItems;
  }

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public CouponEntity getCouponEntityId() {
    return couponEntityId;
  }

  public void setCouponEntityId(CouponEntity couponEntityId) {
    this.couponEntityId = couponEntityId;
  }

  public double getBill() {
    return bill;
  }

  public void setBill(double bill) {
    this.bill = bill;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public PaymentEntity getPaymentEntityId() {
    return paymentEntityId;
  }

  public void setPaymentEntityId(PaymentEntity paymentEntityId) {
    this.paymentEntityId = paymentEntityId;
  }

  public CustomerEntity getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerEntity customer) {
    this.customer = customer;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }

  public RestaurantEntity getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(RestaurantEntity restaurantId) {
    this.restaurantId = restaurantId;
  }
    }
