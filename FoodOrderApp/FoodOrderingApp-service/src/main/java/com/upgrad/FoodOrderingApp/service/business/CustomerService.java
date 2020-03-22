package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CustomerService {

    @Autowired private CustomerDao customerDao;

    @Autowired private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * This method saves the details of the customer with respect to strict validations for successful
     * sign-Up
     * @param customer
     * @return CustomerEntity
     * @throws SignUpRestrictedException
     */
    public CustomerEntity saveCustomer(CustomerEntity customer) throws SignUpRestrictedException {
        if (customer.getContactNumber() != null
                && customerDao.getContactNumber(customer.getContactNumber()) != null) {
            throw new SignUpRestrictedException(
                    "SGR-001", "This contact number is already registered! Try other contact number.");
        }
        if (customer.getFirstName().equals("")
                || customer.getEmail().equals("")
                || customer.getContactNumber().equals("")
                || customer.getPassword().equals("")) {
            throw new SignUpRestrictedException(
                    "SGR-005", "Except last name all fields should be filled");
        }
        if (customer.getEmail() != null) {
            String email = customer.getEmail();
            String emailRegex =
                    "^[a-zA-Z0-9_+&*-]+(?:\\."
                            + "[a-zA-Z0-9_+&*-]+)*@"
                            + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                            + "A-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailRegex);
            if (!pattern.matcher(email).matches()) {
                throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
            }
        }
        if (customer.getContactNumber() != null) {
            String contactNum = customer.getContactNumber();
            String contactNumberRegex = "^[0-9]{10}$";
            Pattern pattern = Pattern.compile(contactNumberRegex);
            if (!pattern.matcher(contactNum).matches()) {
                throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
            }
        }

        if (customer.getPassword() != null) {
            String password = customer.getPassword();
            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$%&*!^])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(passwordRegex);
            if (!pattern.matcher(password).matches()) {
                throw new SignUpRestrictedException("SGR-004", "Weak password!");
            }
        }
        String[] encryptedText = passwordCryptographyProvider.encrypt(customer.getPassword());
        customer.setSalt(encryptedText[0]);
        customer.setPassword(encryptedText[1]);
        customer.setUuid(UUID.randomUUID().toString());

        return customerDao.createCustomer(customer);
    }

    /**
     * This method authenticates a customer by performing Encryption
     * @param userName
     * @param password
     * @return CustomerAuthEntity
     * @throws AuthenticationFailedException
     */
    public CustomerAuthEntity authenticate(String userName, String password) throws AuthenticationFailedException {
        CustomerEntity customer = customerDao.getContactNumber(userName);
        if (customer == null) {
            throw new AuthenticationFailedException("ATH-001","This contact number has not been registered!");
        }
        String encryptedPassword = passwordCryptographyProvider.encrypt(password, customer.getSalt());
        if (encryptedPassword.equals(customer.getPassword())) {
            CustomerAuthEntity customerAuth = new CustomerAuthEntity();
            final ZonedDateTime issuedTime = ZonedDateTime.now();
            final ZonedDateTime expiryTime = ZonedDateTime.now().plusHours(5);
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(password);
            customerAuth.setAccessToken(jwtTokenProvider.generateToken(customer.getUuid(),issuedTime,expiryTime));
            customerAuth.setCustomer(customer);
            customerAuth.setLoginTime(issuedTime);
            customerAuth.setExpiryTime(expiryTime);
            customerAuth.setUuid(UUID.randomUUID().toString());
            return customerDao.createCustomerAuth(customerAuth);
        }
        else{
            throw new AuthenticationFailedException("ATH-002","Invalid Credentials");
        }
    }

    /**
     * This method performs the functionality of Logout
     * @param accessToken
     * @return CustomerAuthEntity
     * @throws AuthorizationFailedException
     */
    public CustomerAuthEntity logout(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuth = customerDao.findByAccessToken(accessToken);
        if (customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }
        else if(hasUserSignedOut(customerAuth.getLogoutTime())){
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        }
        else if(hasSessionExpired(customerAuth.getExpiryTime())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }
        else {
            customerAuth.setLogoutTime(ZonedDateTime.now());
            return customerAuth;
        }
    }

    /**
     * This method updates the Customer Details
     * @param authorization
     * @param newCustomer
     * @return CustomerEntity
     * @throws AuthorizationFailedException
     * @throws UpdateCustomerException
     */
    public CustomerEntity updateCustomer(String authorization, CustomerEntity newCustomer)
            throws AuthorizationFailedException, UpdateCustomerException {
        if (newCustomer.getFirstName() == null) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        String[] authorizationHeader = authorization.split("Bearer ");
        String accessToken = "";
        for (String i : authorizationHeader) {
            accessToken = i;
        }
        CustomerAuthEntity customerAuth = customerDao.findByAccessToken(accessToken);

        if (customerAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (hasUserSignedOut(customerAuth.getLogoutTime())) {
            throw new AuthorizationFailedException(
                    "ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (hasSessionExpired(customerAuth.getExpiryTime())) {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        } else {
            CustomerEntity existingCustomer = customerAuth.getCustomer();
            existingCustomer.setFirstName(newCustomer.getFirstName());
            existingCustomer.setLastName(newCustomer.getLastName());
            return customerDao.update(existingCustomer);
        }
    }

    /**
     * This method updates the password of the customer by matching the Old Passsword with the Password stored
     * in DB
     * @param oldPassword
     * @param newPassword
     * @param customer
     * @return Customer Entity
     * @throws UpdateCustomerException
     */
    public CustomerEntity updateCustomerPassword(
            String oldPassword, String newPassword, CustomerEntity customer)
            throws UpdateCustomerException {
        if (newPassword != "") {
            String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$%&*!^])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(passwordRegex);
            if (!pattern.matcher(newPassword).matches()) {
                throw new UpdateCustomerException("UCR-001", "Weak password!");
            }
        }
        if (oldPassword != "") {
            String encryptedOldPassword =
                    passwordCryptographyProvider.encrypt(oldPassword, customer.getSalt());
            if (!encryptedOldPassword.equals(customer.getPassword())) {
                throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
            }
        }
        // Encrypting the new Password entered by the customer
        String[] encryptedText = passwordCryptographyProvider.encrypt(newPassword);
        customer.setSalt(encryptedText[0]);
        customer.setPassword(encryptedText[1]);
        return customer;
    }

    public CustomerEntity updateCustomer(CustomerEntity updatedCustomer){
        return customerDao.updatePassword(updatedCustomer);
    }

    /**
     * This method gets the accessToken from the Authorization Header and finds the CustomerAuth entity
     *
     * @param accessToken
     * @return Customer Entity
     * */
    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuth = customerDao.findByAccessToken(accessToken);
        if (customerAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (hasUserSignedOut(customerAuth.getLogoutTime())) {
            throw new AuthorizationFailedException(
                    "ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        } else if (hasSessionExpired(customerAuth.getExpiryTime())) {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        } else {
            return customerAuth.getCustomer();
        }
    }

    /**
     * Checks if the customer has signed out by comparing if the current time is after the loggedOutTime
     * received by the method. Returns true if the current-time is after loggedOutTime(Logout has
     * happened), false otherwise
     *
     * @param loggedOutTime
     * @return boolean value true/false
     */
    public boolean hasUserSignedOut(ZonedDateTime loggedOutTime) {
        return (loggedOutTime != null && ZonedDateTime.now().isAfter(loggedOutTime));
    }

    /**
     * Checks if the customer session has expired by comparing if the current time is after the expiry time
     * received by the method. Returns true if the current-time is after expiry time(Session has expired),
     * false otherwise
     * @param expiryTime
     * @return boolean value true/false
     */
    public boolean hasSessionExpired(ZonedDateTime expiryTime) {
        return (expiryTime != null && ZonedDateTime.now().isAfter(expiryTime));
    }
}