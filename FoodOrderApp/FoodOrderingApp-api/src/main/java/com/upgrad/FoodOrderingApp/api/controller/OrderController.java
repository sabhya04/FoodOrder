package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.business.AddressService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/** This class handles all requests related to placing of an order in checkout page */
@RestController
@CrossOrigin(value = "*", maxAge = 1L)
@RequestMapping("/order")
public class OrderController {

  @Autowired private OrderService ordersService;
  @Autowired private CustomerService customerService;
  @Autowired private AddressService addressService;
  @Autowired private RestaurantService restaurantService;
  @Autowired private PaymentService paymentService;
  @Autowired private ItemService itemService;

  /**
   * retrieves a coupon details based upon its name,from static table
   *
   * @param couponName
   * @param accessToken
   * @return
   * @throws AuthorizationFailedException
   * @throws CouponNotFoundException
   */
  @GetMapping(path = "/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(
      @PathVariable("coupon_name") String couponName,
      @RequestHeader("authorization") String accessToken)
      throws AuthorizationFailedException, CouponNotFoundException {

    customerService.getCustomer(accessToken.split("Bearer ")[1]);

    if (couponName != null && couponName.isEmpty()) {
      throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
    }

    /** positive case, after all checks in service* */
    CouponEntity couponEntity = ordersService.getCouponByCouponName(couponName);

    CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
    /** entity to dto* */
    couponDetailsResponse.setCouponName(couponEntity.getCouponName());
    UUID uuid = UUID.fromString(couponEntity.getUuid());
    couponDetailsResponse.setId(uuid);
    couponDetailsResponse.setPercent(couponEntity.getPercent());

    return new ResponseEntity<>(couponDetailsResponse, HttpStatus.OK);
  }

  /**
   * retreives list of all historic orders of a customer
   *
   * @param accessToken
   * @return
   * @throws AuthorizationFailedException
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomerOrderResponse> getPastOrders(
      @RequestHeader("authorization") String accessToken) throws AuthorizationFailedException {
    /** authorization checks* */
    CustomerEntity customerEntity = customerService.getCustomer(accessToken.split("Bearer ")[1]);

    /** positive case, after all checks* */
    List<OrderEntity> list = ordersService.getOrdersByCustomers(customerEntity.getUuid());
    CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
    List<OrderList> orderList = new LinkedList<OrderList>();
    List<ItemQuantityResponse> itemListPerOrder = new LinkedList<ItemQuantityResponse>();
    /** entity to dto* */
    if (list != null) {
      for (OrderEntity entity : list) {
        OrderList orderModel = new OrderList();

        if (entity.getOrderItems() != null) {
          for (OrderItemEntity item : entity.getOrderItems()) {
            ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
            ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
            UUID uuid = UUID.fromString(item.getItemId().getUuid());
            itemQuantityResponseItem.setId(uuid); // how to convert integer to UUID
            itemQuantityResponseItem.setItemName(item.getItemId().getItemName());
            itemQuantityResponseItem.setItemPrice(item.getItemId().getPrice());
            if (item.getItemId().getType().equals("0")) {
              itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.VEG);
            } else {
              itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.NON_VEG);
            }

            itemQuantityResponse.setItem(itemQuantityResponseItem);
            itemQuantityResponse.setPrice(item.getPrice());
            itemQuantityResponse.setQuantity(item.getQuantity());

            itemListPerOrder.add(itemQuantityResponse);
          }
        }
        UUID uuid = UUID.fromString(entity.getUuid());
        orderModel.setId(uuid);

        /** setting address. keep note of nested child objects* */
        OrderListAddress orderListAddress = new OrderListAddress();
        orderListAddress.setCity(entity.getAddress().getCity());
        orderListAddress.setFlatBuildingName(entity.getAddress().getFlatBuilNo());
        UUID addressUuid = UUID.fromString(entity.getAddress().getUuid());
        orderListAddress.setId(addressUuid);
        orderListAddress.setLocality(entity.getAddress().getLocality());
        orderListAddress.setPincode(entity.getAddress().getPinCode());
        OrderListAddressState orderListAddressState = new OrderListAddressState();
        UUID stateUuid = UUID.fromString(entity.getAddress().getState().getUuid());
        orderListAddressState.setId(stateUuid);
        orderListAddressState.setStateName(entity.getAddress().getState().getStateName());
        orderListAddress.setState(orderListAddressState);
        orderModel.setAddress(orderListAddress);

        orderModel.setBill(BigDecimal.valueOf(entity.getBill()));

        OrderListCoupon orderListCoupon = new OrderListCoupon();
        orderListCoupon.setCouponName(entity.getCouponEntityId().getCouponName());
        UUID couponUuid = UUID.fromString(entity.getCouponEntityId().getUuid());
        orderListCoupon.setId(couponUuid);
        orderListCoupon.setPercent(entity.getCouponEntityId().getPercent());
        orderModel.setCoupon(orderListCoupon);

        OrderListCustomer orderListCustomer = new OrderListCustomer();
        orderListCustomer.setContactNumber(entity.getCustomer().getContactNumber());
        orderListCustomer.setEmailAddress(entity.getCustomer().getEmail());
        orderListCustomer.setFirstName(entity.getCustomer().getFirstName());
        orderListCustomer.setLastName(entity.getCustomer().getLastName());
        UUID customerUuid = UUID.fromString(entity.getCustomer().getUuid());
        orderListCustomer.setId(customerUuid);

        orderModel.setCustomer(orderListCustomer);

        OrderListPayment orderListPayment = new OrderListPayment();
        UUID paymentUuid = UUID.fromString(entity.getPaymentEntityId().getUuid());
        orderListPayment.setId(paymentUuid);
        orderListPayment.setPaymentName(entity.getPaymentEntityId().getPaymentName());
        orderModel.setPayment(orderListPayment);

        orderModel.setDate(entity.getDate().toString());
        orderModel.setDiscount(BigDecimal.valueOf(entity.getDiscount()));
        orderModel.setItemQuantities(itemListPerOrder);

        orderList.add(orderModel);
      }
    }

    customerOrderResponse.setOrders(orderList);
    return new ResponseEntity<>(customerOrderResponse, HttpStatus.OK);
  }

  /**
   * save all details of on order , after its placed form checkout page
   *
   * @param placedOrder
   * @param accessToken
   * @return
   * @throws AuthorizationFailedException
   * @throws CouponNotFoundException
   * @throws AddressNotFoundException
   * @throws PaymentMethodNotFoundException
   * @throws RestaurantNotFoundException
   * @throws ItemNotFoundException
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SaveOrderResponse> savePlacedOrder(
      @RequestBody SaveOrderRequest placedOrder, @RequestHeader("authorization") String accessToken)
      throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException,
          PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {

    /** authorization checks* */
    CustomerEntity customerEntity = customerService.getCustomer(accessToken.split("Bearer ")[1]);

    /** positive case, after all checks* */
    OrderEntity ordersEntity = new OrderEntity();
    /** reteiving all required child entity objects* */
    CouponEntity couponEntity =
        ordersService.getCouponByCouponId(placedOrder.getCouponId().toString());
    PaymentEntity paymentEntity =
        paymentService.getPaymentByUUID(placedOrder.getPaymentId().toString());
    AddressEntity addressEntity =
        addressService.getAddressByUUID(placedOrder.getAddressId(), customerEntity);
    RestaurantEntity restaurant =
        restaurantService.restaurantByUUID(placedOrder.getRestaurantId().toString());

    List<ItemQuantity> cartItemlist = placedOrder.getItemQuantities();
    List<OrderItemEntity> items = new LinkedList<>();

    for (ItemQuantity currentItem : cartItemlist) {
      ItemEntity item = itemService.getItemForItemId(currentItem.getItemId().toString());
      OrderItemEntity orderItem = new OrderItemEntity();

      orderItem.setItemId(item);
      orderItem.setOrderId(ordersEntity);
      orderItem.setPrice(currentItem.getPrice());
      orderItem.setQuantity(currentItem.getQuantity());

      items.add(orderItem);
    }

    /** populating the required entity fields* */
    ordersEntity.setUuid(UUID.randomUUID().toString());
    ordersEntity.setBill(placedOrder.getBill().doubleValue());
    ordersEntity.setDiscount(placedOrder.getDiscount().doubleValue());
    Date date = new Date();
    ordersEntity.setDate(date);

    ordersEntity.setAddress(addressEntity);
    ordersEntity.setCouponEntityId(couponEntity);
    ordersEntity.setOrderItems(items);
    ordersEntity.setPaymentEntityId(paymentEntity);
    ordersEntity.setRestaurantId(restaurant);
    ordersEntity.setCustomer(customerEntity);

    ordersService.saveOrder(ordersEntity);

    for (OrderItemEntity itemEntity : ordersEntity.getOrderItems()) {
      ordersService.saveOrderItem(itemEntity);
    }

    SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
    saveOrderResponse.setId(ordersEntity.getUuid());
    saveOrderResponse.setStatus("ORDER SUCCESSFULLY PLACED");
    return new ResponseEntity<>(saveOrderResponse, HttpStatus.CREATED);
  }
    }
