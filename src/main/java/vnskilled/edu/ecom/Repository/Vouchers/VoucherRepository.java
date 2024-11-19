package vnskilled.edu.ecom.Repository.Vouchers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Vouchers.VouchersEntity;
@Repository
public interface VoucherRepository extends JpaRepository<VouchersEntity, Long> {
}
