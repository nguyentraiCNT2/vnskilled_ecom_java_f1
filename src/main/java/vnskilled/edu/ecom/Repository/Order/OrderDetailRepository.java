package vnskilled.edu.ecom.Repository.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Orders.OrderDetailEntity;
import vnskilled.edu.ecom.Entity.Orders.OrderEntity;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
	List<OrderDetailEntity> findByOrderId(OrderEntity orderId);
}
