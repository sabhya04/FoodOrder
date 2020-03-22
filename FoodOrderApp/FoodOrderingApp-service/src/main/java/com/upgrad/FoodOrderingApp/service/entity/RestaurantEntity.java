package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "restaurant")
@NamedQueries({
  @NamedQuery(name = "allRestaurants", query = "select r from RestaurantEntity r"),
  @NamedQuery(
      name = "findRestaurantByName",
      query =
          "select r from RestaurantEntity r where lower(r.restaurantName) like lower(:restaurantName)"),
  @NamedQuery(name = "allCategory", query = "select c from CategoryEntity c"),
  @NamedQuery(
      name = "findRestaurantById",
      query = "select r from RestaurantEntity r where r.uuid = :uuid"),
  @NamedQuery(
      name = "updateRestaurantRating",
      query = "select r from RestaurantEntity r where r.uuid = :uuid")
})
public class RestaurantEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private Integer Id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "restaurant_name")
  @Size(max = 50)
  @NotNull
  private String restaurantName;

  @Column(name = "photo_url")
  @Size(max = 255)
  private String photoUrl;

  @Column(name = "customer_rating")
  @NotNull
  private double customerRating;

  @Column(name = "average_price_for_two")
  @NotNull
  private Integer averagePriceForTwo;

  @Column(name = "number_of_customers_rated")
  @NotNull
  private Integer numberOfCustomersRated;

  @OneToOne(cascade = CascadeType.REMOVE)
  @NotNull
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

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

  public String getRestaurantName() {
    return restaurantName;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public double getCustomerRating() {
    return customerRating;
  }

  public void setCustomerRating(double customerRating) {
    this.customerRating = customerRating;
  }

  public Integer getAveragePriceForTwo() {
    return averagePriceForTwo;
  }

  public void setAveragePriceForTwo(Integer averagePriceForTwo) {
    this.averagePriceForTwo = averagePriceForTwo;
  }

  public Integer getNumberOfCustomersRated() {
    return numberOfCustomersRated;
  }

  public void setNumberOfCustomersRated(Integer numberOfCustomersRated) {
    this.numberOfCustomersRated = numberOfCustomersRated;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }
    }



