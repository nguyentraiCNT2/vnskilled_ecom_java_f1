package vnskilled.edu.ecom.Model.DTO.Products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductSKUDTO {
	private Long id;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private UserDTO createBy;
	private UserDTO updateBy;
	private ProductDTO productId;
	private String image;
	private Long quantity;
	private BigDecimal price;

}
