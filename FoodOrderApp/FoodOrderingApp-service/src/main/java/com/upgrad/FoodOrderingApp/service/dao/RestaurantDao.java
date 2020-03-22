package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method fetches all the restaurants
     */

    public List<RestaurantEntity> getAllRestaurants() {
        List<RestaurantEntity> restaurantList = null;

        TypedQuery<RestaurantEntity> query =
                entityManager.createNamedQuery("allRestaurants", RestaurantEntity.class);
        restaurantList = query.getResultList();

        return restaurantList;
    }

    /**
     * This method fetches all the restaurant by given name from the database
     */

    public List<RestaurantEntity> getRestaurantByName(String restaurantName) {
        List<RestaurantEntity> restaurantList = null;
        TypedQuery<RestaurantEntity> query =
                entityManager.createNamedQuery("findRestaurantByName", RestaurantEntity.class);
        query.setParameter("restaurantName", "%" + restaurantName + "%");

        restaurantList = query.getResultList();
        return restaurantList;
    }

    private List<RestaurantEntity> noList = new ArrayList<>();

    public List<RestaurantEntity> getNoList() {
        return noList;
    }

    public List<CategoryEntity> getAllCategories() {
        List<CategoryEntity> categoryList = null;

        TypedQuery<CategoryEntity> query =
                entityManager.createNamedQuery("allCategory", CategoryEntity.class);
        categoryList = query.getResultList();

        return categoryList;
    }

    public List<RestaurantCategoryEntity> getCategoryByRestaurant(Integer restaurantId) {

        TypedQuery<RestaurantCategoryEntity> query = entityManager.createNamedQuery("getCategoryByRestaurant", RestaurantCategoryEntity.class);
        query.setParameter("restaurantId", restaurantId);
        return query.getResultList();
    }

    /**
     * This method fetches all the restaurant by given categoryId
     */

    public List<RestaurantCategoryEntity> getRestaurantByCategoryId(String uuid) {
        List<RestaurantCategoryEntity> restaurantList = null;
        TypedQuery<RestaurantCategoryEntity> query =
                entityManager.createNamedQuery("findRestaurantByCategoryId", RestaurantCategoryEntity.class);
        query.setParameter("uuid", uuid);
        restaurantList = query.getResultList();
        return restaurantList;
    }

    /**
     * This method fetches all the restaurant by given uuid
     */

    public List<RestaurantEntity> restaurantByUUID(String uuid) {
        List<RestaurantEntity> restaurantList = null;
        TypedQuery<RestaurantEntity> query =
                entityManager.createNamedQuery("findRestaurantById", RestaurantEntity.class);
        query.setParameter("uuid", uuid);
        restaurantList = query.getResultList();
        return restaurantList;

    }

    /**
     * This method fetches all the item for the given restaurant uuid
     */

    public List<ItemEntity> getItemByRestaurant(UUID uuid) {
        List<ItemEntity> items = null;
        TypedQuery<ItemEntity> query =
                entityManager.createNamedQuery("itemsByRestaurant", ItemEntity.class);
        items = query.getResultList();
        return items;

    }

    /**
     * This method fetches all the popular item for a restaurant
     */

    public List<ItemEntity> getItemByPopular(UUID uuid){
        List<ItemEntity> items = null;
        TypedQuery<ItemEntity> query =
                entityManager.createNamedQuery("mostPopularItem", ItemEntity.class);

        items = query.getResultList();
        return items;
    }

    /**
     * This method updates the restaurant rating
     */

    public RestaurantEntity updateRestaurantRating(String restaurantId, BigDecimal customerRating) {

        TypedQuery<RestaurantEntity> query =
                entityManager.createNamedQuery("findRestaurantById", RestaurantEntity.class);
        query.setParameter("uuid", restaurantId);

        RestaurantEntity r = query.getSingleResult();
        BigDecimal existingRating = BigDecimal.valueOf(r.getCustomerRating());
        Integer numberOfCustomers = r.getNumberOfCustomersRated();

        BigDecimal total = existingRating.multiply(new BigDecimal(numberOfCustomers));
        BigDecimal a = total.add(customerRating);
        BigDecimal b= new BigDecimal(numberOfCustomers+1);
        BigDecimal newRating = a.divide(b,2, RoundingMode.HALF_UP);
        r.setCustomerRating(newRating.doubleValue());
        r.setNumberOfCustomersRated(++numberOfCustomers);

        entityManager.persist(r);
        entityManager.flush();

        return r;

    }

}
