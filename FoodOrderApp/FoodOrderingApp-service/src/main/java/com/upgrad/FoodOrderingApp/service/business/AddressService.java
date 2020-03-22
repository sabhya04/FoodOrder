package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AddressService {

  @Autowired private AddressDao addressDao;

  public StateEntity getStateByUUID(String stateUuid) throws AddressNotFoundException {
    StateEntity stateEntity = addressDao.getStateByUuid(stateUuid);
    if (stateEntity == null) {
      throw new AddressNotFoundException("ANF-002", "No state by this id");
    }
    return stateEntity;
  }

  /**
   * This method performs validations and passes the address Entity to the DAO class to save
   *
   * @param address
   * @param customer
   * @return AddressEntity
   * @throws SaveAddressException
   */
  public AddressEntity saveAddress(AddressEntity address, CustomerEntity customer)
      throws SaveAddressException {
    if (address.getFlatBuilNo().equals("")
        || address.getLocality().equals("")
        || address.getCity().equals("")
        || address.getPinCode().equals("")
        || address.getState().getUuid().equals("")) {
      throw new SaveAddressException("SAR-001", "No field can be empty");
    }

    if (!address.getPinCode().equals("")) {
      String pinCode = address.getPinCode();
      String pinCodeRegex = "^[0-9]{6}$";
      Pattern pattern = Pattern.compile(pinCodeRegex);
      if (!pattern.matcher(pinCode).matches()) {
        throw new SaveAddressException("SAR-002", "Invalid pincode");
      }
    }
    address.setUuid(UUID.randomUUID().toString());
    address.setActive(1);
    AddressEntity createdAddress = addressDao.createAddress(address);
    CustomerAddressEntity customerAddress = new CustomerAddressEntity();
    customerAddress.setCustomer(customer);
    customerAddress.setAddress(address);
    addressDao.createCustomerAddress(customerAddress);
    return createdAddress;
  }

  /**
   * This method performs validations and fetches the address from the DAO class by UUID
   * @param addressUuid
   * @param customerEntity
   * @return AddressEntity
   * @throws AddressNotFoundException
   * @throws AuthorizationFailedException
   */
  public AddressEntity getAddressByUUID(String addressUuid, CustomerEntity customerEntity)
      throws AddressNotFoundException, AuthorizationFailedException {
    AddressEntity addressEntity = addressDao.getAddressByUuid(addressUuid);
    if (addressEntity == null) {
      throw new AddressNotFoundException("ANF-003", "No address by this id");
    }
    if (!addressEntity.getCustomerAddress().getCustomer().getId().equals(customerEntity.getId())) {
      throw new AuthorizationFailedException(
          "ATHR-004", "You are not authorized to view/update/delete any one else's address");
    }
    return addressEntity;
  }

    /**
     * This method checks if the address which the user wants to delete is associated with any order or not
     * If yes, the active flag is set to 0, otherwise the address is removed.
     *
     * @param addressEntity
     * @return The addressEntity which is removed
     */
  public AddressEntity deleteAddress(AddressEntity addressEntity) {
    OrderEntity orders = addressDao.getOrderByAddressId(addressEntity);
    if (orders != null) {
      addressEntity.setActive(0);
      return addressEntity;
    } else {
      return addressDao.deleteAddress(addressEntity);
    }
  }

    /**
     * This method retrieves the list of all customer addresses from the CustomerAddress table by taking the
     * customerId
     * @param customerEntity
     * @return List of Address
     */
  public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) {
    List<CustomerAddressEntity> customerAddressList =
        addressDao.getAllAddressByCustomerId(customerEntity);
    List<AddressEntity> addressEntityList = new ArrayList<>();
    for (CustomerAddressEntity customerAddress : customerAddressList) {
      addressEntityList.add(customerAddress.getAddress());
    }
    return addressEntityList;
  }

  /**
   * This method retrieves the List of state entities
   * @return List of State Entity
   */
    public List<StateEntity> getAllStates() {
      if (addressDao.findAllStates() == null){
          return Collections.emptyList();
      }
      else {
          return addressDao.findAllStates();
      }
    }
}
