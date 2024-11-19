package vnskilled.edu.ecom.Model.Response.Product;

import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductTranslationDTO;

import java.util.List;

public class ProductResponse {
	private ProductDTO product;
	private ProductTranslationDTO translation;
	private List<ProductSkuResponse> skus;

	public ProductDTO getProduct () {
		return product;
	}

	public void setProduct (ProductDTO product) {
		this.product = product;
	}

	public ProductTranslationDTO getTranslation () {
		return translation;
	}

	public void setTranslation (ProductTranslationDTO translation) {
		this.translation = translation;
	}

	public List<ProductSkuResponse> getSkus () {
		return skus;
	}

	public void setSkus (List<ProductSkuResponse> skus) {
		this.skus = skus;
	}
}
