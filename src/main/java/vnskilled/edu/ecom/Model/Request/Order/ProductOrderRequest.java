package vnskilled.edu.ecom.Model.Request.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderRequest {
private Long productId;
private int quantity;
}
