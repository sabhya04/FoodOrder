package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** Address entity class */
@Entity
@Table(name = "address")
@NamedQueries({
  @NamedQuery(
      name = "findAddressByUUID",
      query = "Select a from AddressEntity a where a.uuid = :uuid")
})
public class AddressEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private Integer Id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "flat_buil_number")
  @Size(max = 255)
  private String flatBuilNo;

  @Column(name = "locality")
  @Size(max = 255)
  private String locality;

  @Column(name = "city")
  @Size(max = 30)
  private String city;

  @Column(name = "pincode")
  @Size(max = 30)
  private String pinCode;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "state_id", referencedColumnName = "id")
  private StateEntity state;

  @Column(name = "active")
  private Integer active;

  @OneToOne(mappedBy = "address")
  private CustomerAddressEntity customerAddress;

  public AddressEntity() {}

  public AddressEntity(
      @NotNull @Size(max = 200) String uuid,
      @Size(max = 255) String flatNumber,
      @Size(max = 255) String locality,
      @Size(max = 30) String city,
      @Size(max = 30) String pinCode,
      StateEntity state) {
    this.uuid = uuid;
    this.flatBuilNo = flatNumber;
    this.locality = locality;
    this.city = city;
    this.pinCode = pinCode;
    this.state = state;
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

  public String getFlatBuilNo() {
    return flatBuilNo;
  }

  public void setFlatBuilNo(String flatBuilNo) {
    this.flatBuilNo = flatBuilNo;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String pinCode) {
    this.pinCode = pinCode;
  }

  public StateEntity getState() {
    return state;
  }

  public void setState(StateEntity state) {
    this.state = state;
  }

  public CustomerAddressEntity getCustomerAddress() {
    return customerAddress;
  }

  public void setCustomerAddress(CustomerAddressEntity customerAddress) {
    this.customerAddress = customerAddress;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }
    }