package vnskilled.edu.ecom.Controller.Oroder;

import com.google.api.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Exception.ErrorResponse;
import vnskilled.edu.ecom.Model.Request.Order.OrderRequest;
import vnskilled.edu.ecom.Model.Response.Order.OrderResponse;
import vnskilled.edu.ecom.Service.Order.OrderService;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping ("/all")
	public ResponseEntity<?> getAllOrders(@RequestBody Map<String, Integer> pagination) {
		try {
			int page = pagination.getOrDefault("page", 1);
			int size = pagination.getOrDefault("size", 10);
			Pageable pageable = PageRequest.of(page, size);
			List<OrderResponse> orderResponse = orderService.getAllOrders(pageable);
			return ResponseEntity.status (HttpStatus.OK).body (orderResponse);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/user")
	public  ResponseEntity<?> getOrdersByUserId(@RequestBody Map<String, Integer> pagination) {
		try {
			int page = pagination.getOrDefault("page", 1);
			int size = pagination.getOrDefault("size", 10);
			Pageable pageable = PageRequest.of(page, size);
			List<OrderResponse> orderResponse = orderService.getByUserId (pageable);
			return ResponseEntity.status (HttpStatus.OK).body (orderResponse);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.INTERNAL_SERVER_ERROR);		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable Long id) {
		try {
			return ResponseEntity.status (HttpStatus.OK).body (orderService.getOrderById(id));
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/status")
	public ResponseEntity<?> getOrdersByStatus(@RequestParam ("status") String status, @RequestBody Map<String, Integer> pagination) {
		try {
			int page = pagination.getOrDefault("page", 1);
			int size = pagination.getOrDefault("size", 10);
			Pageable pageable = PageRequest.of(page, size);
			return ResponseEntity.status (HttpStatus.OK).body (orderService.getOrdersByStatus(status, pageable));
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/place")
	public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
		try {
			orderService.placeOrder(orderRequest);
			return ResponseEntity.status (HttpStatus.OK).body ("Đặt hàng thành công");
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.BAD_REQUEST);
		}
	}
}
