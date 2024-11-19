package vnskilled.edu.ecom.Service.Shipping;

import org.springframework.data.domain.Pageable;
import vnskilled.edu.ecom.Model.DTO.Shipping.ShippingDTO;

import java.util.List;

public interface ShippingService {
    List<ShippingDTO> getAll();
    void save(ShippingDTO shipping);
}
