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
public class ProductVariantDTO {
	private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private ProductSKUDTO variantId;
    private String language;
	private String variantKey;
	private String value;
	private String lang;

}

