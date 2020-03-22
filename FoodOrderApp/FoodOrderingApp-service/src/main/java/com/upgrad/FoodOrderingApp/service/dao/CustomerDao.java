package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class CustomerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity getContactNumber(String contactNumber){
        try {
            return entityManager.createNamedQuery("findContactNumber", CustomerEntity.class)
                    .setParameter("contactNumber",contactNumber)
                    .getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
        }

        public CustomerEntity createCustomer(CustomerEntity customer) {
            entityManager.persist(customer);
            return customer;
        }

        public CustomerAuthEntity createCustomerAuth(CustomerAuthEntity customerAuth){
        entityManager.persist(customerAuth);
        return customerAuth;
        }

    public CustomerAuthEntity findByAccessToken(String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("customerAuthByAccessToken", CustomerAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerEntity update(CustomerEntity updatedCustomer) {
        entityManager.merge(updatedCustomer);
        return updatedCustomer;
    }

    public CustomerEntity updatePassword(CustomerEntity customer) {
        entityManager.merge(customer);
        return customer;
    }
}
