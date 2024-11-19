package vnskilled.edu.ecom.Model.Response.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.Orders.OrderDTO;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponse {
	private OrderDTO order;
	private List<OrderDetailResponse>  orderDetail;

}
