package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * Payment entity class
 */
@Entity
@Table(name = "payment")
@NamedQueries({
        @NamedQuery(name = "getAllPaymentMethods", query = "select p from PaymentEntity p"),
        @NamedQuery(name = "getPaymentMethodById", query = "select p from PaymentEntity p where p.uuid=:uuid")})
public class PaymentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer Id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "payment_name")
    @Size(max = 255)
    private String paymentName;

    public PaymentEntity() {
    }

    public PaymentEntity(@NotNull @Size(max = 200) String uuid, @Size(max = 255) String paymentName) {
        this.uuid = uuid;
        this.paymentName = paymentName;
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

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
