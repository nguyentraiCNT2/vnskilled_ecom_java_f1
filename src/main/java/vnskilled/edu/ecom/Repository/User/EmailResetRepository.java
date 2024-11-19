package vnskilled.edu.ecom.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.User.EmailResetEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EmailResetRepository extends JpaRepository<EmailResetEntity, Long> {
    List<EmailResetEntity> findByEmail(String email);
    EmailResetEntity findByUserId(UserEntity user_id);
    List<EmailResetEntity> findAllByUserIdAndCreatedAtBetween(UserEntity user, Timestamp start, Timestamp end);
}
