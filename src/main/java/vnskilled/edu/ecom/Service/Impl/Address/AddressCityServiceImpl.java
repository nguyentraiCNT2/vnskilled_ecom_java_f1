package vnskilled.edu.ecom.Service.Impl.Address;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Address.AddressCityEntity;
import vnskilled.edu.ecom.Entity.Address.AddressCountryEntity;
import vnskilled.edu.ecom.Model.DTO.Address.AddressCityDTO;
import vnskilled.edu.ecom.Repository.Address.AddressCityRepository;
import vnskilled.edu.ecom.Repository.Address.AddressCountryRepository;
import vnskilled.edu.ecom.Service.Address.AddressCityService;

import java.util.ArrayList;
import java.util.List;
@Service
public class AddressCityServiceImpl implements AddressCityService {

    @Autowired
    private AddressCityRepository addressCityRepository;
    @Autowired
    private AddressCountryRepository addressCountryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<AddressCityDTO> getAllAddressCity() {
        try {
            List<AddressCityDTO> list = new ArrayList<>();
            List<AddressCityEntity> addressCityEntities = addressCityRepository.findAll();
            for (AddressCityEntity addressCityEntity : addressCityEntities) {
                list.add(modelMapper.map(addressCityEntity, AddressCityDTO.class));

            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void saveAddressCity(AddressCityDTO addressCity) {
        try {
            AddressCountryEntity addressCountryEntity = addressCountryRepository.findById(addressCity.getCountryId().getId()).orElseThrow(() -> new RuntimeException("Country not found"));
            AddressCityEntity addressCityEntity = new AddressCityEntity();
            addressCityEntity.setName(addressCity.getName());
            addressCityEntity.setCountryId(addressCountryEntity);
            addressCityRepository.save(addressCityEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateAddressCity(AddressCityDTO addressCity) {
        try {
            AddressCityEntity addressCityEntity = addressCityRepository.findById(addressCity.getId())
                    .orElseThrow(() -> new RuntimeException("Address NOT FOUND"));
            AddressCountryEntity addressCountryEntity = addressCountryRepository.findById(addressCity.getCountryId().getId()).orElseThrow(() -> new RuntimeException("Country not found"));

            addressCityEntity.setName(addressCity.getName());
            addressCityEntity.setCountryId(addressCountryEntity);
            addressCityRepository.save(addressCityEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteAddressCity(Long id) {
        try {
            AddressCityEntity addressCityEntity = addressCityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address NOT FOUND"));
            addressCityRepository.delete(addressCityEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int totalItem() {
        return (int) addressCityRepository.count();
    }

	@Override
	public List<AddressCityDTO> getByCountry (Long id) {
		try {
			List<AddressCityDTO> list = new ArrayList<>();
			AddressCountryEntity addressCountryEntity = addressCountryRepository.findById(id).orElseThrow (() -> new RuntimeException("Country not found"));
			List<AddressCityEntity> addressCityEntities = addressCityRepository.findByCountryId (addressCountryEntity);
			for (AddressCityEntity addressCityEntity : addressCityEntities) {
				AddressCityDTO addressCityDTO = new AddressCityDTO();
				list.add(modelMapper.map(addressCityEntity, AddressCityDTO.class));

			}
			return list;
		}catch (Exception e){
			throw new RuntimeException (e.getMessage ());
		}
	}
}
