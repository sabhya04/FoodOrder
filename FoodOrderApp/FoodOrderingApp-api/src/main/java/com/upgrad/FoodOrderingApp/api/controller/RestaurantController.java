package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.business.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.api.model.*;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * This is a controller class to handle http request related to restaurant management
 * functionalities
 */
@RestController
@CrossOrigin(value = "*", maxAge = 1L)
public class RestaurantController {

  @Autowired RestaurantService restaurantService;

  @Autowired CustomerService customerService;

  @Autowired CategoryService categoryService;

  /**
   * This method is used to handle http request of user to get all restaurants
   *
   * @return returns ResponseEntity enbedded with model object or error object
   */
  @RequestMapping(method = GET, path = "/restaurant", produces = APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<RestaurantDetailsResponse>> getAllRestaurants() {

    List<RestaurantDetailsResponse> list = new LinkedList<>();
    for (RestaurantEntity restaurantEntity : restaurantService.restaurantsByRating()) {
      RestaurantDetailsResponse response = new RestaurantDetailsResponse();
      response.setId(UUID.fromString(restaurantEntity.getUuid()));
      response.setRestaurantName(restaurantEntity.getRestaurantName());
      response.setPhotoURL(restaurantEntity.getPhotoUrl());
      response.setCustomerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()));
      response.setAveragePrice(restaurantEntity.getAveragePriceForTwo());
      response.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());

      RestaurantDetailsResponseAddress addressResponse = new RestaurantDetailsResponseAddress();
      addressResponse.setCity(restaurantEntity.getAddress().getCity());
      addressResponse.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuilNo());
      addressResponse.setId(UUID.fromString(restaurantEntity.getAddress().getUuid()));
      addressResponse.setLocality(restaurantEntity.getAddress().getLocality());
      addressResponse.setPincode(restaurantEntity.getAddress().getPinCode());

      RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
          new RestaurantDetailsResponseAddressState();
      restaurantDetailsResponseAddressState.setId(
          UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
      restaurantDetailsResponseAddressState.setStateName(
          restaurantEntity.getAddress().getState().getStateName());
      addressResponse.setState(restaurantDetailsResponseAddressState);

      response.setAddress(addressResponse);

      List<CategoryList> restaurantCategory = new LinkedList<>();
      for (RestaurantCategoryEntity category :
          categoryService.getCategoriesByRestaurant(restaurantEntity.getId().toString())) {
        CategoryList restCategory = new CategoryList();
        restCategory.setCategoryName(category.getCategoryId().getCategoryName());
        restaurantCategory.add(restCategory);
        Collections.sort(restaurantCategory, new CategoriesComparator());
      }

      response.setCategories(restaurantCategory);
      list.add(response);
    }

    Collections.sort(list, new CustomerRatingComparator());

    return new ResponseEntity<List<RestaurantDetailsResponse>>(list, HttpStatus.OK);
  }

  /**
   * This method is used to handle http request of user to get restaurants by input name
   *
   * @return returns ResponseEntity enbedded with model object or error object
   * @throws RestaurantNotFoundException
   */
  @RequestMapping(
      method = GET,
      path = "/restaurant/name/{restaurant_name}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurantByName(
      @PathVariable("restaurant_name") final String restaurantName)
      throws RestaurantNotFoundException {

    List<RestaurantEntity> restaurants = restaurantService.restaurantsByName(restaurantName);
    List<RestaurantDetailsResponse> restaurantList = new ArrayList<>();
    for (RestaurantEntity restaurantEntity : restaurants) {

      RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
          new RestaurantDetailsResponseAddressState();
      restaurantDetailsResponseAddressState.setId(
          UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
      restaurantDetailsResponseAddressState.setStateName(
          restaurantEntity.getAddress().getState().getStateName());

      RestaurantDetailsResponseAddress restaurantDetailsResponseAddress =
          new RestaurantDetailsResponseAddress();
      restaurantDetailsResponseAddress.setPincode(restaurantEntity.getAddress().getPinCode());
      restaurantDetailsResponseAddress.setLocality(restaurantEntity.getAddress().getLocality());
      restaurantDetailsResponseAddress.setFlatBuildingName(
          restaurantEntity.getAddress().getFlatBuilNo());
      restaurantDetailsResponseAddress.setCity(restaurantEntity.getAddress().getCity());
      restaurantDetailsResponseAddress.setId(UUID.fromString(restaurantEntity.getUuid()));
      restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

      RestaurantDetailsResponse rList = new RestaurantDetailsResponse();
      rList.setAddress(restaurantDetailsResponseAddress);
      rList.setAveragePrice(restaurantEntity.getAveragePriceForTwo());
      rList.setCustomerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()));
      rList.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());
      rList.setPhotoURL(restaurantEntity.getPhotoUrl());
      rList.setRestaurantName(restaurantEntity.getRestaurantName());
      rList.setId(UUID.fromString(restaurantEntity.getUuid()));

      List<CategoryList> restaurantCategoryName = new LinkedList<>();
      for (RestaurantCategoryEntity restaurantCategory1 :
          categoryService.getCategoriesByRestaurant(restaurantEntity.getId().toString())) {
        CategoryList categoryList = new CategoryList();
        categoryList.setCategoryName(restaurantCategory1.getCategoryId().getCategoryName());
        restaurantCategoryName.add(categoryList);

        Collections.sort(restaurantCategoryName, new CategoriesComparator());
      }

      rList.setCategories(restaurantCategoryName);
      restaurantList.add(rList);
    }

    Collections.sort(restaurantList, new CustomerRatingComparator());
    return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantList, HttpStatus.OK);
  }

  /**
   * This method is used to handle http request of user to get restaurants by given categoryId
   *
   * @return returns ResponseEntity embedded with model object or error object
   * @throws CategoryNotFoundException
   */
  @RequestMapping(
      method = GET,
      path = "/restaurant/category/{category_id}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurantByCategoryId(
      @PathVariable("category_id") final String categoryId) throws CategoryNotFoundException {

    List<RestaurantCategoryEntity> restaurantByCategoryId =
        restaurantService.restaurantByCategory(categoryId);

    List<RestaurantDetailsResponse> restaurantList = new LinkedList<>();
    for (RestaurantCategoryEntity category : restaurantByCategoryId) {
      RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
      restaurantDetailsResponse.setId(UUID.fromString(category.getRestaurantId().getUuid()));
      restaurantDetailsResponse.setRestaurantName(category.getRestaurantId().getRestaurantName());
      restaurantDetailsResponse.setPhotoURL(category.getRestaurantId().getPhotoUrl());
      restaurantDetailsResponse.setAveragePrice(category.getRestaurantId().getAveragePriceForTwo());
      restaurantDetailsResponse.setCustomerRating(BigDecimal.valueOf(category.getRestaurantId().getCustomerRating()));
      restaurantDetailsResponse.setNumberCustomersRated(
          category.getRestaurantId().getNumberOfCustomersRated());

      RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
          new RestaurantDetailsResponseAddressState();
      restaurantDetailsResponseAddressState.setId(
          UUID.fromString(category.getRestaurantId().getAddress().getState().getUuid()));
      restaurantDetailsResponseAddressState.setStateName(
          category.getRestaurantId().getAddress().getState().getStateName());

      RestaurantDetailsResponseAddress addressResponse = new RestaurantDetailsResponseAddress();
      addressResponse.setCity(category.getRestaurantId().getAddress().getCity());
      addressResponse.setFlatBuildingName(category.getRestaurantId().getAddress().getFlatBuilNo());
      addressResponse.setId(UUID.fromString(category.getRestaurantId().getAddress().getUuid()));
      addressResponse.setLocality(category.getRestaurantId().getAddress().getLocality());
      addressResponse.setPincode(category.getRestaurantId().getAddress().getPinCode());
      addressResponse.setState(restaurantDetailsResponseAddressState);
      restaurantDetailsResponse.setAddress(addressResponse);

      List<CategoryList> nameResponses = new ArrayList<>();
      for (RestaurantCategoryEntity categoryName :
          categoryService.getCategoriesByRestaurant(
              category.getRestaurantId().getId().toString())) {
        CategoryList categoryNameResponse = new CategoryList();
        categoryNameResponse.setCategoryName(categoryName.getCategoryId().getCategoryName());
        nameResponses.add(categoryNameResponse);

        Collections.sort(nameResponses, new CategoriesComparator());
      }
      restaurantDetailsResponse.setCategories(nameResponses);
      restaurantList.add(restaurantDetailsResponse);
    }

    Collections.sort(restaurantList, new CustomerRatingComparator());

    return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantList, HttpStatus.OK);
  }

  /**
   * This method used to handle http request of user to get restaurant by input restaurantId
   *
   * @return returns ResponseEntity enbedded with model object or error object
   * @throws RestaurantNotFoundException
   */
  @RequestMapping(
      method = GET,
      path = "api/restaurant/{restaurant_id}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurantByUuid(
      @PathVariable("restaurant_id") final String uuid) throws RestaurantNotFoundException {

    List<RestaurantEntity> restaurants = restaurantService.restaurantUUID(uuid);

    List<RestaurantDetailsResponse> restaurantLists = new ArrayList<>();

    for (RestaurantEntity restaurantEntity : restaurants) {

      RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState =
          new RestaurantDetailsResponseAddressState();
      restaurantDetailsResponseAddressState.setId(
          UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
      restaurantDetailsResponseAddressState.setStateName(
          restaurantEntity.getAddress().getState().getStateName());

      RestaurantDetailsResponseAddress restaurantDetailsResponseAddress =
          new RestaurantDetailsResponseAddress();
      restaurantDetailsResponseAddress.setPincode(restaurantEntity.getAddress().getPinCode());
      restaurantDetailsResponseAddress.setLocality(restaurantEntity.getAddress().getLocality());
      restaurantDetailsResponseAddress.setFlatBuildingName(
          restaurantEntity.getAddress().getFlatBuilNo());
      restaurantDetailsResponseAddress.setCity(restaurantEntity.getAddress().getCity());
      restaurantDetailsResponseAddress.setId(UUID.fromString(restaurantEntity.getUuid()));
      restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

      RestaurantDetailsResponse restList = new RestaurantDetailsResponse();

      restList.setAddress(restaurantDetailsResponseAddress);
      restList.setAveragePrice(restaurantEntity.getAveragePriceForTwo());
      restList.setCustomerRating(BigDecimal.valueOf(restaurantEntity.getCustomerRating()));
      restList.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());
      restList.setPhotoURL(restaurantEntity.getPhotoUrl());
      restList.setRestaurantName(restaurantEntity.getRestaurantName());
      restList.setId((UUID.fromString(restaurantEntity.getUuid())));

      List<CategoryList> categoryLists = new ArrayList<>();
      for (RestaurantCategoryEntity restaurantCategory :
          categoryService.getCategoriesByRestaurant(restaurantEntity.getId().toString())) {
        CategoryList categoryList = new CategoryList();
        categoryList.setCategoryName(restaurantCategory.getCategoryId().getCategoryName());
        categoryLists.add(categoryList);

        Collections.sort(categoryLists, new CategoriesComparator());
      }

      restList.setCategories(categoryLists);
      restaurantLists.add(restList);
    }

    Collections.sort(restaurantLists, new CustomerRatingComparator());

    return new ResponseEntity<List<RestaurantDetailsResponse>>(restaurantLists, HttpStatus.OK);
  }

  /**
   * This method is used to handle http request of user to update the restaurant rating
   *
   * @param customerRating
   * @return returns ResponseEntity enbedded with model object or error object
   * @throws AuthorizationFailedException,RestaurantNotFoundException,InvalidRatingException
   */
  @RequestMapping(
      method = PUT,
      path = "/api/restaurant/{restaurant_id}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantRatings(
      @PathVariable("restaurant_id") final String restaurantId,
      @RequestParam("customer_rating") final Double customerRating,
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException, RestaurantNotFoundException, InvalidRatingException {

    RestaurantEntity restaurant = new RestaurantEntity();
    restaurant.setUuid(restaurant.getUuid());
    restaurant.setCustomerRating(restaurant.getCustomerRating());

    customerService.getCustomer(accessToken);

    restaurantService.updateRestaurantRating(restaurantId, customerRating);

    RestaurantUpdatedResponse restaurantUpdatedResponse =
        new RestaurantUpdatedResponse()
            .id(UUID.fromString(restaurantId))
            .status("RESTAURANT RATING UPDATED SUCCESSFULLY");

    return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
  }

  /** comparator method used to sort restaurants by ratings */
  static class CustomerRatingComparator implements Comparator<RestaurantDetailsResponse> {

    @Override
    public int compare(RestaurantDetailsResponse rating1, RestaurantDetailsResponse rating2) {

      return rating1.getCustomerRating().compareTo(rating2.getCustomerRating());
    }
  }

  /** comparator method used to sort categories based on alphabetical order */
  static class CategoriesComparator implements Comparator<CategoryList> {

    @Override
    public int compare(CategoryList category1, CategoryList category2) {

      return category1.getCategoryName().compareTo(category2.getCategoryName());
    }
  }
        }

