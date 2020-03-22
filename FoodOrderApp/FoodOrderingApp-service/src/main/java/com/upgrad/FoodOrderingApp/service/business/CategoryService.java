package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.common.GenericExceptionCode;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/** This service class manages all functionalities and business rules of category management */

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    /**
     * This method manages business rules for getting all categories ordered by name
     *
     */

    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        return categoryDao.getAllCategories();
    }

    /**
     * This method manages business rules for getting category ordered by categoryId
     *
     */
    public List<CategoryEntity> getCategoryById(String categoryId) throws CategoryNotFoundException {

        if(categoryId.isEmpty()){
            throw new CategoryNotFoundException(
                    GenericExceptionCode.CNF_001.getCode(),
                    GenericExceptionCode.CNF_001.getDescription());
        }

        else if(categoryId.equals(" ")){
            throw new CategoryNotFoundException(
                    GenericExceptionCode.CNF_002.getCode(),
                    GenericExceptionCode.CNF_002.getDescription());
        }

        return categoryDao.getCategoryById(categoryId);
    }

    /**
     * This method manages business rules for getting all categories by restaurantId
     *
     */

    public List<RestaurantCategoryEntity> getCategoriesByRestaurant(String restaurantId) {
        return categoryDao.getCategoryByRestaurant(Integer.parseInt(restaurantId));
    }

}
