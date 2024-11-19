package vnskilled.edu.ecom.Repository.Otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Otp.OtpTemplateEntity;

import java.util.Optional;

@Repository
public interface OtpTemplateRepository extends JpaRepository<OtpTemplateEntity, Long> {
	Optional<OtpTemplateEntity> findByType(String type);
}
