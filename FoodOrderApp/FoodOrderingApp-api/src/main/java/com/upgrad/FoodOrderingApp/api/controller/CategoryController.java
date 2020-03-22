package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.business.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(value = "*", maxAge = 1L)
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ItemService itemService;

    /**
     * This method used to handle http request of user to get all Categories
     *
     * @return returns ResponseEntity enbedded with model object or error object
     */

    @RequestMapping(
            method = GET,
            path = "/category",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )

    public ResponseEntity<List<CategoryList>> getAllCategories(){

        List<CategoryList> list = new LinkedList<>();

        for(CategoryEntity categoryEntity: categoryService.getAllCategoriesOrderedByName()){
            CategoryList categoryListResponse = new CategoryList();

            categoryListResponse.setCategoryName(categoryEntity.getCategoryName());
            categoryListResponse.setId(UUID.fromString(categoryEntity.getUuid()));

            list.add(categoryListResponse);
        }
        Collections.sort(list,new RestaurantController.CategoriesComparator());
        return new ResponseEntity<List<CategoryList>>(list, HttpStatus.OK);
    }


    /**
     * This method used to handle http request of user to get Category by categoryId
     *
     * @return returns ResponseEntity enbedded with model object or error object
     * @throws CategoryNotFoundException
     */

    @RequestMapping(
            method = GET,
            path = "/category/{category_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<List<CategoryList>> getCategoryById(
            @PathVariable("category_id") final String categoryId) throws CategoryNotFoundException{

        List<CategoryEntity> categories = categoryService.getCategoryById(categoryId);

        List<CategoryList> categoryLists = new ArrayList<>();

        for(CategoryEntity category:categories){

            CategoryList categoryList = new CategoryList();
            categoryList.setCategoryName(category.getCategoryName());
            categoryList.setId(UUID.fromString(category.getUuid()));

            List<ItemList> itemLists = new ArrayList<>();
            for(CategoryItemEntity categoryItem : itemService.getItemByCategory(categoryId)){

                ItemList itemList = new ItemList();
                ItemList.ItemTypeEnum itemTypeEnum = null;
                if(categoryItem.getItemId().getType().equals("0")){
                    itemTypeEnum= ItemList.ItemTypeEnum.VEG;
                }
                else{
                    itemTypeEnum =  ItemList.ItemTypeEnum.NON_VEG;
                }

                itemList.setItemType(itemTypeEnum);
                itemList.setPrice(categoryItem.getItemId().getPrice());
                itemList.setItemName(categoryItem.getItemId().getItemName());
                itemList.setId(UUID.fromString(categoryItem.getItemId().getUuid()));

                itemLists.add(itemList);
            }

            categoryList.setItemList(itemLists);

            categoryLists.add(categoryList);
        }

        return new ResponseEntity<List<CategoryList>>(categoryLists,HttpStatus.OK);
    }
}
