package vnskilled.edu.ecom.Service.Impl.Address;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Address.AddressCityEntity;
import vnskilled.edu.ecom.Entity.Address.AddressWardsEntity;
import vnskilled.edu.ecom.Model.DTO.Address.AddressWardsDTO;
import vnskilled.edu.ecom.Repository.Address.AddressCityRepository;
import vnskilled.edu.ecom.Repository.Address.AddressWardsRepository;
import vnskilled.edu.ecom.Service.Address.AddressWardService;

import java.util.ArrayList;
import java.util.List;
@Service
public class AddressWardServiceImpl implements AddressWardService {

    @Autowired
    private AddressWardsRepository addressWardsRepository;
    @Autowired
    private AddressCityRepository addressCityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<AddressWardsDTO> getAllAddressWards() {
        try {
            List<AddressWardsDTO> list = new ArrayList<>();
            List<AddressWardsEntity> addressWardsEntities = addressWardsRepository.findAll();
            for (AddressWardsEntity addressWardsEntity : addressWardsEntities) {
                list.add(modelMapper.map(addressWardsEntity, AddressWardsDTO.class));

            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void saveAddressWards(AddressWardsDTO wardsDTO) {
        try {
            AddressCityEntity addressCityEntity = addressCityRepository.findById(wardsDTO.getCityId().getId()).orElseThrow(() -> new RuntimeException("Country not found"));
            AddressWardsEntity countryEntity = new AddressWardsEntity();
            countryEntity.setName(wardsDTO.getName());
            countryEntity.setCityId(addressCityEntity);
            addressWardsRepository.save(countryEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateAddressWards(AddressWardsDTO wardsDTO) {
        try {
            AddressWardsEntity addressWardsEntity = addressWardsRepository.findById(wardsDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Country not found"));
            addressWardsEntity.setName(wardsDTO.getName());
            addressWardsRepository.save(addressWardsEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteAddressWards(Long id) {
        try {
            AddressWardsEntity wardsEntity = addressWardsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address NOT FOUND"));
            addressWardsRepository.delete(wardsEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int totalItem() {
        return (int) addressWardsRepository.count();
    }


	@Override
	public List<AddressWardsDTO> getByCity (Long id) {
		try {
			List<AddressWardsDTO> list = new ArrayList<>();
			AddressCityEntity addressCityEntity = addressCityRepository.findById(id).orElseThrow (() -> new RuntimeException("Country not found"));
			List<AddressWardsEntity> addressWardsEntities = addressWardsRepository.findByCityId (addressCityEntity);
			for (AddressWardsEntity addressWardsEntity : addressWardsEntities) {
				list.add(modelMapper.map(addressWardsEntity, AddressWardsDTO.class));

			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}
}
