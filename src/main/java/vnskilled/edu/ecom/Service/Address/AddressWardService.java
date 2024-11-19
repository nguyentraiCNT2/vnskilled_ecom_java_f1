package vnskilled.edu.ecom.Service.Address;

import org.springframework.data.domain.Pageable;

import vnskilled.edu.ecom.Model.DTO.Address.AddressWardsDTO;

import java.util.List;

public interface AddressWardService {
    List<AddressWardsDTO> getAllAddressWards();
    void saveAddressWards(AddressWardsDTO wardsDTO);
    void updateAddressWards(AddressWardsDTO wardsDTO);
    void deleteAddressWards(Long id);
    int totalItem();
	List<AddressWardsDTO> getByCity(Long id);
}
