package vnskilled.edu.ecom.Model.Response.Product;

import vnskilled.edu.ecom.Model.DTO.Products.ProductSKUDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductVariantDTO;

import java.util.List;
import java.util.Map;

public class ProductSkuResponse {
	private ProductSKUDTO productSKU;
	private Map<String, String> variants;

	public ProductSKUDTO getProductSKU () {
		return productSKU;
	}

	public void setProductSKU (ProductSKUDTO productSKU) {
		this.productSKU = productSKU;
	}

	public Map<String, String> getVariants () {
		return variants;
	}

	public void setVariants (Map<String, String> variants) {
		this.variants = variants;
	}
}
