package vnskilled.edu.ecom.Model.Request.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductSkuVariantRequest {
	private String image;
	private Long quantity;
	private BigDecimal price;
	private String variantKey;
	private String value;
	private String lang;
	private Long productId;

}
