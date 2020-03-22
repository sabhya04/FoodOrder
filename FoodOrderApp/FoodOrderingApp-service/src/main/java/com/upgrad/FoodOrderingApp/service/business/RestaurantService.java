package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.common.GenericExceptionCode;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurentRepository;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


/** This service class manages all functionalities and business rules of restaurant management */

@Service
@Transactional(propagation = Propagation.REQUIRED)

public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurentRepository restaurentRepository;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CategoryDao categoryDao;


    /**
     * This method manages business rules to get restaurant ordered by their rating
     *
     */
    public List<RestaurantEntity> restaurantsByRating() {
        return restaurantDao.getAllRestaurants();
    }

    public List<CategoryEntity> getCategoryName(String categoryName){
        return categoryDao.getCategoryNameById(categoryName);
    }

    /**
     * This method manages business rules to get restaurant by input name
     *
     */
    public List<RestaurantEntity> restaurantsByName(String restaurantName)
            throws RestaurantNotFoundException {
        if (restaurantName.isEmpty()) {
            throw new RestaurantNotFoundException(
                    GenericExceptionCode.RNF_003.getCode(),
                    GenericExceptionCode.RNF_003.getDescription());
        }
        return restaurantDao.getRestaurantByName(restaurantName);

    }

    /**
     * This method manages business rules to get restaurant by given category
     *
     */

    public List<RestaurantCategoryEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {
        if (categoryId.isEmpty()) {
            throw new CategoryNotFoundException(
                    GenericExceptionCode.CNF_001.getCode(),
                    GenericExceptionCode.CNF_001.getDescription());
        } else if (categoryId.equals(" ")) {
            throw new CategoryNotFoundException(
                    GenericExceptionCode.CNF_002.getCode(),
                    GenericExceptionCode.CNF_002.getDescription());
        }

        return restaurantDao.getRestaurantByCategoryId(categoryId);
    }

    /**
     * This method manages business rules to get restaurant by their input uuid
     *
     */
    public List<RestaurantEntity> restaurantUUID(String someRestaurantId) throws RestaurantNotFoundException {

        if (someRestaurantId.isEmpty()) {
            throw new RestaurantNotFoundException(
                    GenericExceptionCode.RNF_002.getCode(),
                    GenericExceptionCode.RNF_002.getDescription());
        } else if (someRestaurantId.equals(" ")) {
            throw new RestaurantNotFoundException(
                    GenericExceptionCode.RNF_001.getCode(),
                    GenericExceptionCode.RNF_001.getDescription());
        }
        return restaurantDao.restaurantByUUID(someRestaurantId);
    }

    public RestaurantEntity restaurantByUUID(String id) throws RestaurantNotFoundException {


        RestaurantEntity restaurantEntity = restaurentRepository.getRestaurent(id);
        if (restaurantEntity == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        return restaurantEntity;
    }


    /**
     * This method manages business rules to update restaurant rating
     *
     */

    public RestaurantEntity updateRestaurantRating(String restaurantId, Double customerRating)
            throws RestaurantNotFoundException,InvalidRatingException {


        if (restaurantId.isEmpty()) {
            throw new RestaurantNotFoundException(
                    GenericExceptionCode.RNF_002.getCode(),
                    GenericExceptionCode.RNF_002.getDescription());
        } else if (restaurantId.equals(" ")) {
            throw new RestaurantNotFoundException(
                    GenericExceptionCode.RNF_001.getCode(),
                    GenericExceptionCode.RNF_001.getDescription());
        } else if (customerRating == null || (customerRating < 1 && customerRating > 5)) {
            throw new InvalidRatingException(
                    GenericExceptionCode.IRE_001.getCode(),
                    GenericExceptionCode.IRE_001.getDescription());
        } else {

            return restaurantDao.updateRestaurantRating(restaurantId, BigDecimal.valueOf(customerRating));
        }

    }

}
