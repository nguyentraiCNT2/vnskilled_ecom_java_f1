package vnskilled.edu.ecom.Service.Impl.Address;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Address.AddressCountryEntity;
import vnskilled.edu.ecom.Model.DTO.Address.AddressCountryDTO;
import vnskilled.edu.ecom.Repository.Address.AddressCountryRepository;
import vnskilled.edu.ecom.Service.Address.AddressCountryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressCountryServiceImpl implements AddressCountryService {
    @Autowired
    AddressCountryRepository addressCountryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AddressCountryDTO> getAllAddressCountry() {
        try {
            List<AddressCountryDTO> list = new ArrayList<>();
            List<AddressCountryEntity> addressCityEntities = addressCountryRepository.findAll();
            for (AddressCountryEntity addressCountryEntity : addressCityEntities) {
                AddressCountryDTO addressCountryDTO = new AddressCountryDTO();
                list.add(modelMapper.map(addressCountryEntity, AddressCountryDTO.class));

            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void saveAddressCountry(AddressCountryDTO addressCountryDTO) {
        try {
            AddressCountryEntity countryEntity = new AddressCountryEntity();
            countryEntity.setName(addressCountryDTO.getName());
            addressCountryRepository.save(countryEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateAddressCountry(AddressCountryDTO addressCountryDTO) {
        try {
            AddressCountryEntity addressCountryEntity = addressCountryRepository.findById(addressCountryDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Country not found"));
            addressCountryEntity.setName(addressCountryDTO.getName());
            addressCountryRepository.save(addressCountryEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteAddressCountry(Long id) {
        try {
            AddressCountryEntity countryEntity = addressCountryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address NOT FOUND"));
            addressCountryRepository.delete(countryEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int totalItem() {
        return (int) addressCountryRepository.count();
    }
}
