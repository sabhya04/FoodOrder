package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.business.ItemService;
import com.upgrad.FoodOrderingApp.service.business.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller

public class ItemController {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ItemService itemService;

    /**
     * This method used to handle http request of user to get items based on their popularity
     *
     * @return returns ResponseEntity enbedded with model object or error object
     * @throws RestaurantNotFoundException
     */

    @RequestMapping(
            method = GET,
            path = "/item/restaurant/{restaurant_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<List<ItemList>> getItemByRestaurantId(
            @PathVariable("restaurant_id") final UUID restaurantId)
            throws RestaurantNotFoundException {

        List<ItemEntity> restaurantListResponses = itemService.getItemsByPopularity(restaurantId);

        List<ItemList> itemLists = new ArrayList<>();

        for (ItemEntity restaurantItem : restaurantListResponses) {

            ItemList itemList = new ItemList();

            ItemList.ItemTypeEnum itemTypeEnum = null;
            if(restaurantItem.getType().equals("0")){
                itemTypeEnum= ItemList.ItemTypeEnum.VEG;
            }
            else{
                itemTypeEnum =  ItemList.ItemTypeEnum.NON_VEG;
            }

            itemList.setItemType(itemTypeEnum);
            itemList.setId(UUID.fromString(restaurantItem.getUuid()));
            itemList.setItemName(restaurantItem.getItemName());
            itemList.setPrice(restaurantItem.getPrice());

            itemLists.add(itemList);

        }
        return new ResponseEntity<List<ItemList>>(itemLists, HttpStatus.OK);
    }
}
