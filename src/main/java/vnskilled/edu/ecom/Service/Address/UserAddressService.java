package vnskilled.edu.ecom.Service.Address;

import vnskilled.edu.ecom.Model.DTO.Address.UserAddressDTO;

import java.util.List;

public interface UserAddressService {
    List<UserAddressDTO> getByUserId();
    void addUserAddress(UserAddressDTO userAddressDTO);
    void updateUserAddress(UserAddressDTO userAddressDTO);
    void deleteUserAddress(Long id);
}
