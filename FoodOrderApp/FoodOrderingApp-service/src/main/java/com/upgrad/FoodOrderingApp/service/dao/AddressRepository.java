package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Address repository class
 */
@Repository
public class AddressRepository {
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * getAddress
     * @param uuid
     * @return
     */
    public AddressEntity getAddress(String uuid) {
        AddressEntity addressEntity = null;
        try {

            TypedQuery<AddressEntity> query = entityManager.createNamedQuery("findAddressByUuid", AddressEntity.class);
            query.setParameter("uuid", uuid);
            addressEntity = query.getSingleResult();

        } catch (NoResultException e) {


        }
        return addressEntity;
    }

    /**
     * getCustomer
     * @param customerId
     * @param addressId
     * @return
     */
    public CustomerAddressEntity getCustomer(Integer customerId,Integer addressId) {
        CustomerAddressEntity customerAddressEntity =null;
        try {
            TypedQuery<CustomerAddressEntity> query = entityManager.createNamedQuery("customerByAdressId", CustomerAddressEntity.class);
            query.setParameter("customerId",customerId);
            query.setParameter("addressId", addressId);

            customerAddressEntity = query.getSingleResult();
        } catch (Exception e) {


        }
        return customerAddressEntity;


    }

}
