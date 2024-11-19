package vnskilled.edu.ecom.Repository.Otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Otp.OtpEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

    List<OtpEntity> findByUserId(UserEntity user);
}
