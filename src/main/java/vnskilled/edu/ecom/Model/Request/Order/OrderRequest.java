package vnskilled.edu.ecom.Model.Request.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	private String address;
	private String phone;
	private String country;
	private String city;
	private String wards;
	private Long shippingId;
	private Long paymentId;
	private Long voucherId;
	private List<ProductOrderRequest> products;

}
