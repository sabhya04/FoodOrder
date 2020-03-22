package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/** Coupon entity class */
@Entity
@Table(name = "coupon")
@NamedQueries({
  @NamedQuery(
      name = "findCouponByCouponName",
      query = "select c from CouponEntity c where c.couponName=:couponName"),
  @NamedQuery(
      name = "findCouponByCouponId",
      query = "select c from CouponEntity c where c.uuid=:uuid")
})
public class CouponEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private Integer Id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "coupon_name")
  @Size(max = 255)
  private String couponName;

  @Column(name = "percent")
  @NotNull
  private Integer percent;

  public CouponEntity(
      @NotNull @Size(max = 200) String uuid,
      @Size(max = 255) String couponName,
      @NotNull Integer percent) {
    this.uuid = uuid;
    this.couponName = couponName;
    this.percent = percent;
  }

  public CouponEntity() {}

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

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }

  public Integer getPercent() {
    return percent;
  }

  public void setPercent(Integer percent) {
    this.percent = percent;
  }
    }
