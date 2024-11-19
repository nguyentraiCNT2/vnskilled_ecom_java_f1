package vnskilled.edu.ecom.Service.Payment;

import org.springframework.data.domain.Pageable;
import vnskilled.edu.ecom.Model.DTO.Payment.PaymentDTO;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> getAll();
    PaymentDTO getById(Long id);
    void save(PaymentDTO payment);
    void update(Long id, PaymentDTO payment);
    void delete(Long id);
}
