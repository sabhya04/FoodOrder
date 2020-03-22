package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 *  CustomerAddress entity class
 *  */

@Entity
@Table(name = "customer_address")
@NamedQueries({
  @NamedQuery(
      name = "findAllAddressByCustomerId",
      query = "Select c from CustomerAddressEntity c where c.customer.Id = :customerId"),
  @NamedQuery(
      name = "customerByAdressId",
      query =
          "select ca from CustomerAddressEntity ca where ca.address.Id=:addressId and ca.customer.Id=:customerId")
})
public class CustomerAddressEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer Id;

  @OneToOne(cascade = CascadeType.REMOVE)
  @NotNull
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @ManyToOne(cascade = CascadeType.REMOVE)
  @NotNull
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private CustomerEntity customer;

  public Integer getId() {
    return Id;
  }

  public void setId(Integer id) {
    Id = id;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }

  public CustomerEntity getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerEntity customer) {
    this.customer = customer;
  }
    }
