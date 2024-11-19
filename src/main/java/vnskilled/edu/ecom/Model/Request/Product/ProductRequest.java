package vnskilled.edu.ecom.Model.Request.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {
	private boolean published;
	private Long categoryId;
	private boolean hotProduct;
	private boolean newProduct;
	private List<MultipartFile> imagelist;
	private List<MultipartFile> videolist;
	private MultipartFile image;
	private Long quantity;
	private BigDecimal price;
	private String language;
	private String name;
	private String description;
	private String variantKey;
	private String value;
	private String lang;
}
