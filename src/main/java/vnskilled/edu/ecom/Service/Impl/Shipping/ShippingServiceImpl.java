package vnskilled.edu.ecom.Service.Impl.Shipping;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Shipping.ShippingEntity;
import vnskilled.edu.ecom.Model.DTO.Shipping.ShippingDTO;
import vnskilled.edu.ecom.Repository.Shipping.ShippingRepository;
import vnskilled.edu.ecom.Service.Shipping.ShippingService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ShippingDTO> getAll() {
        try {
            List<ShippingEntity> shippings = shippingRepository.findAll();
            return shippings.stream()
                    .map(shipping -> modelMapper.map(shipping, ShippingDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void save(ShippingDTO shipping) {
        try {
            ShippingEntity shippingEntity = modelMapper.map(shipping, ShippingEntity.class);
            shippingRepository.save(shippingEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
