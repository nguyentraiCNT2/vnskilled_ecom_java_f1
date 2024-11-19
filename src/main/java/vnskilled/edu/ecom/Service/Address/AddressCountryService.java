package vnskilled.edu.ecom.Service.Address;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Model.DTO.Address.AddressCountryDTO;

import java.util.List;

@Service
public interface AddressCountryService {
    List<AddressCountryDTO> getAllAddressCountry();

    void saveAddressCountry(AddressCountryDTO addressCountryDTO);

    void updateAddressCountry(AddressCountryDTO addressCountryDTO);

        void deleteAddressCountry(Long id);
    int totalItem();
}
