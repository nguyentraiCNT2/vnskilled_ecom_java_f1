package vnskilled.edu.ecom.Model.DTO.Carts;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.Carts.ShoppingCartEntity;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private Long id;
	private ProductSKUEntity productId;
    private int quantity;
	@JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdAt;
	@JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp updatedAt;
    private BigDecimal price;
	private ShoppingCartDTO cart;

}
