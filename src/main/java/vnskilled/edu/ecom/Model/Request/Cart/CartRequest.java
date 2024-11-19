package vnskilled.edu.ecom.Model.Request.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartRequest {
    private Long productId;
    private int quantity;

}
