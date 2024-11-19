package vnskilled.edu.ecom.Model.Response.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.Orders.OrderDetailDTO;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
	private OrderDetailDTO orderDetail;
	private ProductResponse product;
}
