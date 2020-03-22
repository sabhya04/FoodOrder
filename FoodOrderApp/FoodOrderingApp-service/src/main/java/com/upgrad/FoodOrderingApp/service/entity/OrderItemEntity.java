package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 * OrderItem entity class
 */
@Entity
@Table(name = "order_item")
public class OrderItemEntity {

      @Id
      @Column(name = "id")
      @GeneratedValue(strategy = GenerationType.IDENTITY)

      private Integer Id;

      @ManyToOne(cascade = CascadeType.REMOVE)
      @NotNull
      @JoinColumn(name = "order_id", referencedColumnName = "id")
      private OrderEntity orderId;

      @OneToOne
      @NotNull
      @JoinColumn(name = "item_id", referencedColumnName = "id")
      private ItemEntity itemId;

      @Column(name = "quantity")
      @NotNull
      private Integer quantity;

      @Column(name = "price")
      @NotNull
      private Integer price;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public OrderEntity getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderEntity orderId) {
        this.orderId = orderId;
    }

    public ItemEntity getItemId() {
        return itemId;
    }

    public void setItemId(ItemEntity itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}


