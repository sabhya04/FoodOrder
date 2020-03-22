package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 1L)
@RequestMapping("/")
public class CustomerController {

  @Autowired private CustomerService customerService;

  /**
   * This method This method accepts an HTTP method of POST type and is used to sign-up a customer using
   * the  SignupCustomerRequest model. The corresponding fields of the user are set using the Request
   * model fields. These parameters are passed to the Service layer where the business logic
   * implementation takes place. Response Entity generic class provided Spring is used to map the
   * SignupCustomerResponse as an object. It produces a JSON Response,with HTTP status code as =
   * CREATED(201) and corresponding message
   *
   * @param signupCustomerRequest
   * @return Sign up Customer Response model & HTTP status in a Response Entity object
   * @throws SignUpRestrictedException
   */
  @PostMapping(
      value = "/customer/signup",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupCustomerResponse> customerSignUp(
      @RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest)
      throws SignUpRestrictedException {

    if (signupCustomerRequest.getFirstName().equals("")
        || signupCustomerRequest.getEmailAddress().equals("")
        || signupCustomerRequest.getContactNumber().equals("")
        || signupCustomerRequest.getPassword().equals("")) {
      throw new SignUpRestrictedException(
          "SGR-005", "Except last name all fields should be filled");
      }
    CustomerEntity customer = new CustomerEntity();
    customer.setFirstName(signupCustomerRequest.getFirstName());
    customer.setLastName(signupCustomerRequest.getLastName());
    customer.setEmail(signupCustomerRequest.getEmailAddress());
    customer.setContactNumber(signupCustomerRequest.getContactNumber());
    customer.setPassword(signupCustomerRequest.getPassword());

    CustomerEntity createdCustomer = customerService.saveCustomer(customer);

    SignupCustomerResponse signupCustomerResponse =
        new SignupCustomerResponse()
            .id(createdCustomer.getUuid())
            .status("CUSTOMER SUCCESSFULLY REGISTERED");

    return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
  }

  /**
   * This method accepts an HTTP method of POST type and is used to authenticate a user on Login The
   * access token of the Signed in User is provided as an input and is decoded and passed to the
   * Service Layer for authentication purpose. Response Entity generic class provided by Spring is
   * used to map the LoginResponse as an object. It produces a JSON Response,with HTTP status code
   * as = OK(200) and corresponding message
   *
   * @param authorizationHeader
   * @return LoginResponse model with HTTP header, HTTP status in a Response Entity object
   * @throws AuthenticationFailedException
   */
  @PostMapping(
      value = "/customer/login",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<LoginResponse> customerLogin(
      @RequestHeader("authorization") String authorizationHeader)
      throws AuthenticationFailedException {

    byte[] decode = Base64.getDecoder().decode(authorizationHeader.split("Basic ")[1]);
    String decodedText = new String(decode);
    int index = decodedText.indexOf(":");
    if (decodedText.substring(index, decodedText.length() - 1).equals("")
        || index == 0) {
      throw new AuthenticationFailedException(
          "ATH-003", "Incorrect format of decoded customer name and password");
    }
    String[] credentials = decodedText.split(":");
    String userName = credentials[0];
    String password = credentials[1];
    CustomerAuthEntity customerAuth = customerService.authenticate(userName, password);
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setId(customerAuth.getCustomer().getUuid());
    loginResponse.setMessage("LOGGED IN SUCCESSFULLY");
    loginResponse.setFirstName(customerAuth.getCustomer().getFirstName());
    loginResponse.setLastName(customerAuth.getCustomer().getLastName());
    loginResponse.setEmailAddress(customerAuth.getCustomer().getEmail());
    loginResponse.setContactNumber(customerAuth.getCustomer().getContactNumber());
    HttpHeaders headers = new HttpHeaders();
    headers.set("access-token", customerAuth.getAccessToken());
    List<String> header = new ArrayList<>();
    header.add("access-token");
    headers.setAccessControlExposeHeaders(header);
    return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }

  /**
   * This method accepts a method of POST request and the customer is logged out if the accessToken is present
   * in the DB
   * @param authorization
   * @return LogoutResponse model with HTTP status in a Response Entity object
   * @throws AuthorizationFailedException
   */
  @PostMapping(
      value = "/customer/logout",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<LogoutResponse> customerLogout(
      @RequestHeader("authorization") String authorization) throws AuthorizationFailedException {
      String[] authorizationHeader = authorization.split("Bearer ");
      String accessToken = "";
      for (String i : authorizationHeader) {
          accessToken = i;
      }
    CustomerAuthEntity customerAuth = customerService.logout(accessToken);
    LogoutResponse logoutResponse =
        new LogoutResponse()
            .id(customerAuth.getCustomer().getUuid())
            .message("LOGGED OUT SUCCESSFULLY");
    return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
  }

  /**
   * This method accepts a request of PUT type and is used to update the details of the Customer
   * Consumes JSON consisting of FirstName and LastName of the user & produces a JSON response
   * @param updateCustomerRequest
   * @param authorization
   * @return UpdateCustomerResponse, HTTP Status in a ResponseEntity object
   * @throws AuthorizationFailedException
   * @throws UpdateCustomerException
   */
  @PutMapping(
      value = "/customer",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UpdateCustomerResponse> customerUpdate(
      @RequestBody final UpdateCustomerRequest updateCustomerRequest,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, UpdateCustomerException {
    CustomerEntity customer = new CustomerEntity();
    customer.setFirstName(updateCustomerRequest.getFirstName());
    customer.setLastName(updateCustomerRequest.getLastName());
    CustomerEntity updatedCustomer = customerService.updateCustomer(authorization, customer);
    UpdateCustomerResponse updateCustomerResponse =
        new UpdateCustomerResponse()
            .id(updatedCustomer.getUuid())
            .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY")
            .firstName(updatedCustomer.getFirstName())
            .lastName(updatedCustomer.getLastName());
    return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
  }

  /**
   * This method accepts a request of PUT type and updates the password of a customer
   * @param updatePasswordRequest
   * @param authorization
   * @return UpdatePasswordResponse, HTTP Status in a ResponseEntity object
   * @throws UpdateCustomerException
   * @throws AuthorizationFailedException
   */
  @PutMapping(
      value = "/customer/password",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UpdatePasswordResponse> customerPasswordUpdate(
      @RequestBody final UpdatePasswordRequest updatePasswordRequest,
      @RequestHeader("authorization") String authorization)
      throws UpdateCustomerException, AuthorizationFailedException {
      String[] authorizationHeader = authorization.split("Bearer ");
      String accessToken = "";
      for (String i : authorizationHeader) {
          accessToken = i;
      }
    String oldPassword = updatePasswordRequest.getOldPassword();
    String newPassword = updatePasswordRequest.getNewPassword();
    if (oldPassword.equals("") || newPassword.equals("")) {
      throw new UpdateCustomerException("UCR-003","No field should be empty");
    }
    CustomerEntity customer = customerService.getCustomer(accessToken);
    CustomerEntity updatedCustomer =
        customerService.updateCustomerPassword(oldPassword, newPassword, customer);
    customerService.updateCustomer(updatedCustomer);
    UpdatePasswordResponse updatePasswordResponse =
        new UpdatePasswordResponse()
            .id(updatedCustomer.getUuid())
            .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
    return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse,HttpStatus.OK);
  }
    }
