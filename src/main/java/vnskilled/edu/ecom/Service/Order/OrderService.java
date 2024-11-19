package vnskilled.edu.ecom.Service.Order;

import org.springframework.data.domain.Pageable;
import vnskilled.edu.ecom.Model.Request.Order.OrderRequest;
import vnskilled.edu.ecom.Model.Response.Order.OrderResponse;

import java.util.List;

public interface OrderService {
	List<OrderResponse> getAllOrders(Pageable pageable);
	List<OrderResponse> getByUserId(Pageable pageable);
	OrderResponse getOrderById(Long id);

	List<OrderResponse> getOrdersByStatus(String status,Pageable pageable);
	void placeOrder(OrderRequest orderRequest);



}
