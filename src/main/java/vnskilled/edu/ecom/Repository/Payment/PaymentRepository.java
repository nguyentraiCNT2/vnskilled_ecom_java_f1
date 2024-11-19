package vnskilled.edu.ecom.Repository.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import vnskilled.edu.ecom.Entity.Payment.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
}
