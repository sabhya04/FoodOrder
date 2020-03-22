package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "customer_auth")
@NamedQueries({
        @NamedQuery(name = "customerAuthByAccessToken", query = "Select c from CustomerAuthEntity c where c.accessToken = :accessToken")
})
public class CustomerAuthEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @OneToOne(cascade = CascadeType.REMOVE)
    @NotNull
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerEntity customer;

    @Column(name = "access_token")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @Column(name = "login_at")
    @Basic
    private ZonedDateTime loginTime;

    @Column(name = "logout_at")
    @Basic
    private ZonedDateTime logoutTime;

    @Column(name = "expires_at")
    @Basic
    private ZonedDateTime expiryTime;

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

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) { this.customer = customer; }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(ZonedDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public ZonedDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(ZonedDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}