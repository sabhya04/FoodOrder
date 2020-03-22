package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Customer entity class
 * */
@Entity
@Table(name = "customer")
@NamedQueries({
  @NamedQuery(
      name = "findContactNumber",
      query = "Select c from CustomerEntity c where c.contactNumber = :contactNumber"),
  @NamedQuery(
      name = "getCustomerByUuid",
      query = "select c from CustomerEntity c where c.uuid=:customerUuid")
})
public class CustomerEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer Id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "firstname")
  @Size(max = 30)
  @NotNull
  private String firstName;

  @Column(name = "lastname")
  @Size(max = 30)
  private String lastName;

  @Column(name = "email")
  @Size(max = 50)
  private String email;

  @Column(name = "contact_number")
  @Size(max = 30)
  @NotNull
  private String contactNumber;

  @Column(name = "password")
  @Size(max = 255)
  @NotNull
  private String password;

  @Column(name = "salt")
  @Size(max = 255)
  @NotNull
  private String salt;

  @OneToMany(mappedBy = "customer")
  private List<CustomerAddressEntity> customerAddress;

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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public List<CustomerAddressEntity> getCustomerAddress() {
    return customerAddress;
  }

  public void setCustomerAddress(List<CustomerAddressEntity> customerAddress) {
    this.customerAddress = customerAddress;
  }
    }
