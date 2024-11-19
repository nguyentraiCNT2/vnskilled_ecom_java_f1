package vnskilled.edu.ecom.Service.Address;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Model.DTO.Address.AddressCityDTO;

import java.util.List;

@Service
public interface AddressCityService {
    List<AddressCityDTO> getAllAddressCity();

    void saveAddressCity(AddressCityDTO addressCity);

    void updateAddressCity(AddressCityDTO addressCity);

    void deleteAddressCity(Long id);

    int totalItem();
	List<AddressCityDTO> getByCountry(Long id);
}
