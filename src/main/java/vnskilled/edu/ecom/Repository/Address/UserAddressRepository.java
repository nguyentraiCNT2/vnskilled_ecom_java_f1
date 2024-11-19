package vnskilled.edu.ecom.Repository.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Address.UserAddressEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {
    List<UserAddressEntity> findByUserId(UserEntity userId);
}
