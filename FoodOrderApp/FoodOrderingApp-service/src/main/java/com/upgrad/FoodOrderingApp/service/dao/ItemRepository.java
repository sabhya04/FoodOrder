package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Item repository class
 */
@Repository
public class ItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * getItemById
     * @param uuid
     * @return
     */
    public ItemEntity getItemById(String uuid) {
        ItemEntity itemEntity = null;
        try {
            TypedQuery<ItemEntity> query = entityManager.createNamedQuery("findItemByUuid", ItemEntity.class);
            query.setParameter("uuid", uuid);
            itemEntity = query.getSingleResult();
        } catch (NoResultException e) {

        }
        return itemEntity;

    }
}
