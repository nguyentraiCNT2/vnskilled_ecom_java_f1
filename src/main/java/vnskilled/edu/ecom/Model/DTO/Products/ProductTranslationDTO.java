package vnskilled.edu.ecom.Model.DTO.Products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductTranslationDTO {
    private Long id;
    private String language;
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
	private ProductDTO productId;

}
