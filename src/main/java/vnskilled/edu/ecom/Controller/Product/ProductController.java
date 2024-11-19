package vnskilled.edu.ecom.Controller.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Exception.ErrorResponse;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductTranslationDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductVariantDTO;
import vnskilled.edu.ecom.Model.Request.Product.ProductRequest;
import vnskilled.edu.ecom.Model.Request.Product.ProductSkuVariantRequest;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;
import vnskilled.edu.ecom.Service.Product.ProductService;
import vnskilled.edu.ecom.Util.EndpointConstant.ProductApiPaths;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ProductApiPaths.BASE_PATH)
public class ProductController {

	@Autowired
	private ProductService productService;

	// Lấy tất cả sản phẩm theo ngôn ngữ
	@GetMapping(ProductApiPaths.GET_ALL_PATH)
	public ResponseEntity<?> getAllProducts() {
		try {
			List<ProductResponse> products = productService.getAllProductsByLanguage();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping(ProductApiPaths.GET_NEW_PRODUCTS_PATH)
	public ResponseEntity<?> getNewProducts() {
		try {
			List<ProductResponse> products = productService.getProductByNew();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Phương thức lấy danh sách 8 sản phẩm hot
	@GetMapping(ProductApiPaths.GET_HOT_PRODUCTS_TOP8_PATH)
	public ResponseEntity<?> getHotProductsTop8() {
		try {
			List<ProductResponse> products = productService.getProductByHotLimitTop8();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// Tạo sản phẩm mới
	@PostMapping(ProductApiPaths.CREATE)
	public ResponseEntity<?> createProduct(@ModelAttribute ProductRequest request) {
		try {
			productService.createProduct(request);
			return new ResponseEntity<>(Map.of("message", "Thêm mới thành công"), HttpStatus.CREATED);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Lấy thông tin chi tiết sản phẩm theo ID
	@GetMapping(ProductApiPaths.GET_PRODUCT_BY_ID)
	public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
		try {
			ProductResponse product = productService.getProductById(id);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Cập nhật sản phẩm
	@PutMapping(ProductApiPaths.UPDATE_PRODUCT)
	public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO) {
		try {
			productService.updateProduct(productDTO);
			return new ResponseEntity<>(Map.of("message", "Cập nhật thành công"), HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Xóa sản phẩm
	@DeleteMapping(ProductApiPaths.DELETE_PRODUCT)
	public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
		try {
			productService.deleteProduct(id);
			return new ResponseEntity<>(Map.of("message", "Xóa thành công"), HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Tạo bản dịch cho sản phẩm
	@PostMapping(ProductApiPaths.CREATE_TRANSLATION)
	public ResponseEntity<?> createTranslation(@RequestBody ProductTranslationDTO translationDTO) {
		try {
			productService.createTranslation(translationDTO);
			return new ResponseEntity<>(Map.of("message", "Tạo bản dịch thành công"), HttpStatus.CREATED);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Cập nhật bản dịch
	@PutMapping(ProductApiPaths.UPDATE_TRANSLATION)
	public ResponseEntity<?> updateTranslation(@PathVariable("id") Long id, @RequestBody ProductTranslationDTO translationDTO) {
		try {
			productService.updateTranslation(id, translationDTO);
			return new ResponseEntity<>(Map.of("message", "Cập nhật bản dịch thành công"), HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Tạo SKU cho sản phẩm
	@PostMapping(ProductApiPaths.CREATE_SKU)
	public ResponseEntity<?> createSku(@RequestBody ProductSkuVariantRequest request) {
		try {
			productService.createSku(request);
			return new ResponseEntity<>(Map.of("message", "Tạo SKU thành công"), HttpStatus.CREATED);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Cập nhật SKU
	@PutMapping(ProductApiPaths.UPDATE_SKU)
	public ResponseEntity<?> updateSku(@PathVariable("id") Long id, @RequestBody ProductSkuVariantRequest request) {
		try {
			productService.updateSku(id, request);
			return new ResponseEntity<>(Map.of("message", "Cập nhật SKU thành công"), HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Xóa SKU
	@DeleteMapping(ProductApiPaths.DELETE_SKU)
	public ResponseEntity<?> deleteSku(@PathVariable("id") Long id) {
		try {
			productService.deleteSku(id);
			return new ResponseEntity<>(Map.of("message", "Xóa SKU thành công"), HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Tạo variant cho sản phẩm
	@PostMapping(ProductApiPaths.CREATE_VARIANT)
	public ResponseEntity<?> createVariant(@RequestBody ProductVariantDTO variantDTO) {
		try {
			productService.createVariant(variantDTO);
			return new ResponseEntity<>(Map.of("message", "Tạo variant thành công"), HttpStatus.CREATED);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Cập nhật variant
	@PutMapping(ProductApiPaths.UPDATE_VARIANT)
	public ResponseEntity<?> updateVariant(@RequestBody ProductVariantDTO variantDTO) {
		try {
			productService.updateVariant(variantDTO);
			return new ResponseEntity<>(Map.of("message", "Cập nhật variant thành công"), HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Xóa variant
	@DeleteMapping(ProductApiPaths.DELETE_VARIANT)
	public ResponseEntity<?> deleteVariant(@PathVariable("id") Long id) {
		try {
			productService.deleteVariant(id);
			return new ResponseEntity<>(Map.of("message", "Xóa variant thành công"), HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// API để nhập hàng (import product)
	@PostMapping(ProductApiPaths.IMPORT_PRODUCT)
	public ResponseEntity<?> importProduct(@RequestBody ProductRequest request) {
		try {
			productService.imPortProduct(request);
			return ResponseEntity.ok("Product imported successfully!");
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(ProductApiPaths.EXPORT_PRODUCT)
	public ResponseEntity<?> exportProductsToExcel() {
		try {
			String downloadUrl = productService.exportProductToExcelAndUpload();
			return ResponseEntity.ok(downloadUrl);
		} catch (IOException e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

