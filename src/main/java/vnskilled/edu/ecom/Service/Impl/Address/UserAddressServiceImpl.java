package vnskilled.edu.ecom.Service.Impl.Address;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Address.AddressCityEntity;
import vnskilled.edu.ecom.Entity.Address.AddressCountryEntity;
import vnskilled.edu.ecom.Entity.Address.AddressWardsEntity;
import vnskilled.edu.ecom.Entity.Address.UserAddressEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.Address.UserAddressDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Repository.Address.AddressCityRepository;
import vnskilled.edu.ecom.Repository.Address.AddressCountryRepository;
import vnskilled.edu.ecom.Repository.Address.AddressWardsRepository;
import vnskilled.edu.ecom.Repository.Address.UserAddressRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.Address.UserAddressService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AddressCityRepository addressCityRepository;
    @Autowired
    private AddressCountryRepository addressCountryRepository;
    @Autowired
    private AddressWardsRepository addressWardsRepository;

    @Override
    public List<UserAddressDTO> getByUserId() {
        try {
            List<UserAddressDTO> userAddressDTOS = new ArrayList<>();
         String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findByEmail(email);
            List<UserAddressEntity> userAddressEntities = userAddressRepository.findByUserId(userEntity);
            userAddressEntities.stream()
                    .forEach(userAddressEntity -> {
                        UserAddressDTO userAddressDTO = modelMapper.map(userAddressEntity, UserAddressDTO.class);
                        userAddressDTOS.add(userAddressDTO);
                    });

            return userAddressDTOS;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addUserAddress(UserAddressDTO userAddressDTO) {
        try {
            if(userAddressDTO.getAddress() == null || userAddressDTO.getAddress() == "")
                throw new RuntimeException("Địa chỉ giao hàng khong duoc bo trong");
            if (userAddressDTO.getPhone() == null || userAddressDTO.getPhone() == "")
                throw new RuntimeException("so dien thoai khong duoc bo trong");
            if (userAddressDTO.getCountryId().getId() == null )
                throw new RuntimeException("Ban chua chon quoc gia");
            if (userAddressDTO.getCityId().getId() == null )
                throw new RuntimeException("Ban chua chon thanh pho");
            if (userAddressDTO.getWardId().getId() == null )
                throw new RuntimeException("Ban chua chon quan/huyen");
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findByEmail(email);
            UserAddressEntity userAddressEntity = modelMapper.map(userAddressDTO, UserAddressEntity.class);
            AddressCityEntity addressCityEntity = addressCityRepository.findById(userAddressDTO.getCityId().getId()).orElseThrow(() -> new RuntimeException("City Not Found"));
            AddressCountryEntity addressCountryEntity = addressCountryRepository.findById(userAddressDTO.getCountryId().getId()).orElseThrow(() -> new RuntimeException("Country Not Found"));
            AddressWardsEntity addressWardsEntity= addressWardsRepository.findById(userAddressDTO.getWardId().getId()).orElseThrow(() -> new RuntimeException("Ward Not Found"));
            userAddressEntity.setCityId(addressCityEntity);
            userAddressEntity.setCountryId(addressCountryEntity);
            userAddressEntity.setWardId(addressWardsEntity);
            userAddressEntity.setUserId(userEntity);
            userAddressEntity.setCreatedAt(Timestamp.from(Instant.now()));
            userAddressRepository.save(userAddressEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateUserAddress(UserAddressDTO userAddressDTO) {
        try {
            if(userAddressDTO.getAddress() == null || userAddressDTO.getAddress() == "")
                throw new RuntimeException("Địa chỉ giao hàng khong duoc bo trong");
            if (userAddressDTO.getPhone() == null || userAddressDTO.getPhone() == "")
                throw new RuntimeException("so dien thoai khong duoc bo trong");
            if (userAddressDTO.getCountryId().getId() == null )
                throw new RuntimeException("Ban chua chon quoc gia");
            if (userAddressDTO.getCityId().getId() == null )
                throw new RuntimeException("Ban chua chon thanh pho");
            if (userAddressDTO.getWardId().getId() == null )
                throw new RuntimeException("Ban chua chon quan/huyen");
            UserAddressEntity userAddressEntity=userAddressRepository.findById(userAddressDTO.getId ()).orElseThrow(() -> new RuntimeException("User Not Found"));
            AddressCityEntity addressCityEntity = addressCityRepository.findById(userAddressDTO.getCityId().getId()).orElseThrow(() -> new RuntimeException("City Not Found"));
            AddressCountryEntity addressCountryEntity = addressCountryRepository.findById(userAddressDTO.getCountryId().getId()).orElseThrow(() -> new RuntimeException("Country Not Found"));
            AddressWardsEntity addressWardsEntity= addressWardsRepository.findById(userAddressDTO.getWardId().getId()).orElseThrow(() -> new RuntimeException("Ward Not Found"));
            userAddressEntity.setCityId(addressCityEntity);
            userAddressEntity.setCountryId(addressCountryEntity);
            userAddressEntity.setWardId(addressWardsEntity);
            userAddressEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            userAddressRepository.save(userAddressEntity);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteUserAddress(Long id) {
        try {
            UserAddressEntity userAddressEntity = userAddressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address with ID: " + id + " not found"));
            userAddressRepository.delete(userAddressEntity);

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting address: " + e.getMessage());
        }
    }

}
