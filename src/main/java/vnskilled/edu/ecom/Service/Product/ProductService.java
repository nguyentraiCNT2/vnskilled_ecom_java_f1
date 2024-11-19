package vnskilled.edu.ecom.Service.Product;

import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductTranslationDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductVariantDTO;
import vnskilled.edu.ecom.Model.Request.Product.ProductRequest;
import vnskilled.edu.ecom.Model.Request.Product.ProductSkuVariantRequest;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;

import java.io.IOException;
import java.util.List;

public interface ProductService {
	List<ProductResponse> getAllProductsByLanguage();
	List<ProductResponse> getProductByNew();
	List<ProductResponse> getProductByHotLimitTop8();
	void createProduct(ProductRequest request);
	ProductResponse getProductById(Long id);
	void updateProduct(ProductDTO request);
	void deleteProduct(Long id);
	void updateTranslation(Long id , ProductTranslationDTO  translation);
	void createTranslation(ProductTranslationDTO translation);
	void deleteTranslation(Long id);
	void createSku(ProductSkuVariantRequest request);
	void deleteSku(Long id);
	void updateSku(Long id,ProductSkuVariantRequest request);
	void createVariant(ProductVariantDTO productVariant);
	void deleteVariant(Long id);
	void updateVariant(ProductVariantDTO productVariant);
	void imPortProduct(ProductRequest  request );
	String exportProductToExcelAndUpload() throws IOException;
}
