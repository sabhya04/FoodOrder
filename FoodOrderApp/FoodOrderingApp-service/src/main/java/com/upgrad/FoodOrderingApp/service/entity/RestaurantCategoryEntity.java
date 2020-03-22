package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant_category")
@NamedQueries({
        @NamedQuery(
                name = "getCategoryByRestaurant",
                query = "select rc from RestaurantCategoryEntity rc where rc.restaurantId.Id = :restaurantId"),
        @NamedQuery(
                name = "findRestaurantByCategoryId",
                query = "select rc from RestaurantCategoryEntity rc join CategoryEntity c on rc.categoryId.Id = c.Id where c.uuid =   :uuid")
})


public class RestaurantCategoryEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private Integer Id;

  @OneToOne(cascade = CascadeType.REMOVE)
  @NotNull
  @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
  private RestaurantEntity restaurantId;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @NotNull
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private CategoryEntity categoryId;

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public RestaurantEntity getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(RestaurantEntity restaurantId) {
    this.restaurantId = restaurantId;
  }

  public CategoryEntity getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(CategoryEntity categoryId) {
    this.categoryId = categoryId;
  }
}
