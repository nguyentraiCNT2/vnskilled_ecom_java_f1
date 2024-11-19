package vnskilled.edu.ecom.Repository.Order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Orders.OrderEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
	List<OrderEntity> findByUserId (UserEntity userId, Pageable pageable);
	List<OrderEntity> findByStatus(String status, Pageable pageable);
}
