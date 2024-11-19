package vnskilled.edu.ecom.Service.Impl.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Payment.PaymentEntity;
import vnskilled.edu.ecom.Model.DTO.Payment.PaymentDTO;
import vnskilled.edu.ecom.Repository.Payment.PaymentRepository;
import vnskilled.edu.ecom.Service.Payment.PaymentService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<PaymentDTO> getAll() {
        try {
            return paymentRepository.findAll().stream()
                    .map(paymentEntity -> {
                        PaymentDTO paymentDTO = new PaymentDTO();
                        paymentDTO.setName(paymentEntity.getName());
                        paymentDTO.setDescription(paymentEntity.getDescription());
                        paymentDTO.setCreatedAt(paymentEntity.getCreatedAt());
                        paymentDTO.setUpdatedAt(paymentEntity.getUpdatedAt());
                        return paymentDTO;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public PaymentDTO getById(Long id) {
        try {
            PaymentEntity paymentEntity = paymentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(paymentEntity.getId());
            paymentDTO.setName(paymentEntity.getName());
            paymentDTO.setDescription(paymentEntity.getDescription());
            paymentDTO.setCreatedAt(paymentEntity.getCreatedAt());
            paymentDTO.setUpdatedAt(paymentEntity.getUpdatedAt());

            return paymentDTO;

        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }


    @Override
    public void save(PaymentDTO payment) {
        try {
            PaymentEntity paymentEntity = new PaymentEntity();
            paymentEntity.setName(payment.getName());
            paymentEntity.setDescription(payment.getDescription());
            paymentEntity.setCreatedAt(Timestamp.from(Instant.now()));
            paymentEntity.setUpdatedAt(null);
            paymentRepository.save(paymentEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void update(Long id, PaymentDTO payment) {
        try {
            PaymentEntity paymentEntity = paymentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

            // Cập nhật các thuộc tính
            paymentEntity.setName(payment.getName());
            paymentEntity.setDescription(payment.getDescription());
            paymentEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            paymentRepository.save(paymentEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (!paymentRepository.existsById(id)) {
                throw new RuntimeException("Payment not found with id: " + id);
            }
            paymentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
