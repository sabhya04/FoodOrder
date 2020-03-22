package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 * RestaurentItem entity class
 */
@Entity
@Table(name = "restaurant_item")
public class RestaurantItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer Id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @NotNull
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private ItemEntity itemId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NotNull
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private RestaurantEntity restaurantId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public ItemEntity getItemId() {
        return itemId;
    }

    public void setItemId(ItemEntity itemId) {
        this.itemId = itemId;
    }

    public RestaurantEntity getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(RestaurantEntity restaurantId) {
        this.restaurantId = restaurantId;
    }

}



