package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Repository

public class CategoryDao {

    @PersistenceContext private EntityManager entityManager;

    /**
     * This method fetches all the categories
     */

    public List<CategoryEntity> getAllCategories(){
        List<CategoryEntity> categoryList = null;

        TypedQuery<CategoryEntity> query =
                entityManager.createNamedQuery("allCategories",CategoryEntity.class);
        categoryList = query.getResultList();
        return categoryList;
    }

    /**
     * This method fetches the category for the given Uuid from the database
     */

    public List<CategoryEntity> getCategoryById(String uuid){
        List<CategoryEntity> categoryList = null;

        TypedQuery<CategoryEntity> query =
                entityManager.createNamedQuery("categoryById",CategoryEntity.class);
        query.setParameter("uuid",uuid);
        categoryList = query.getResultList();
        return categoryList;
    }

    /**
     * This method fetches the category for the given name from the database
     */
    public List<CategoryEntity> getCategoryNameById(String categoryName){
        List<CategoryEntity> categoryList = null;

        TypedQuery<CategoryEntity> query =
                entityManager.createNamedQuery("categoryName",CategoryEntity.class);
        query.setParameter("categoryName",categoryName);
        categoryList = query.getResultList();
        return categoryList;
    }

    /**
     * This method fetches the category for the given restaurantId from the database
     */

    public List<RestaurantCategoryEntity> getCategoryByRestaurant(Integer restaurantId){

        TypedQuery<RestaurantCategoryEntity> query = entityManager.createNamedQuery("getCategoryByRestaurant", RestaurantCategoryEntity.class);
        query.setParameter("restaurantId",restaurantId);
        return query.getResultList();
    }

    /**
     * This method fetches the items for the given categoryId from the database
     */
    public List<CategoryItemEntity> getItemByCategoryId(String uuid){

        TypedQuery<CategoryItemEntity> query = entityManager.createNamedQuery("getItemByCategory",CategoryItemEntity.class);
        query.setParameter("uuid",uuid);
        return query.getResultList();
    }
}
