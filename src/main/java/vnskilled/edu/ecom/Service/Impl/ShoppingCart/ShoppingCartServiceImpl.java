package vnskilled.edu.ecom.Service.Impl.ShoppingCart;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Carts.CartItemEntity;
import vnskilled.edu.ecom.Entity.Carts.ShoppingCartEntity;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;
import vnskilled.edu.ecom.Entity.Products.ProductTranslationEntity;
import vnskilled.edu.ecom.Entity.Products.ProductVariantEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.Carts.CartItemDTO;
import vnskilled.edu.ecom.Model.DTO.Carts.ShoppingCartDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductSKUDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductTranslationDTO;
import vnskilled.edu.ecom.Model.Request.Cart.CartRequest;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Response.Cart.CartItemResponse;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;
import vnskilled.edu.ecom.Model.Response.Product.ProductSkuResponse;
import vnskilled.edu.ecom.Repository.Categories.CategoryRepository;
import vnskilled.edu.ecom.Repository.Product.ProductRepository;
import vnskilled.edu.ecom.Repository.Product.ProductSKURepository;
import vnskilled.edu.ecom.Repository.Product.ProductTranslantionRepository;
import vnskilled.edu.ecom.Repository.Product.ProductVariantRepository;
import vnskilled.edu.ecom.Repository.ShoppingCart.CartItemRepository;
import vnskilled.edu.ecom.Repository.ShoppingCart.ShoppingCartRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.FirebaseStorageService;
import vnskilled.edu.ecom.Service.ShoppingCart.ShoppingCartService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductSKURepository productSKURepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductTranslantionRepository productTranslantionRepository;
	@Autowired
	private ProductVariantRepository productVariantRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ModelMapper modelMapper;


	@Override
	public List<CartItemResponse> getCartItemByUser() {
		try {
			List<CartItemResponse> cartItemResponseList = new ArrayList<>();
			CartItemResponse cartItemResponse = new CartItemResponse();
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity = userRepository.findByEmail(email);
			List<CartItemEntity> cartItemEntities = cartItemRepository.findByCart(userEntity.getCart().getId());
			for (CartItemEntity cartItem : cartItemEntities) {
				cartItemResponse.setItem(modelMapper.map(cartItem, CartItemDTO.class));
				ProductSKUEntity product = productSKURepository.findById(cartItem.getProductId().getProductId().getId())
						.orElseThrow(() -> new RuntimeException("Product Not Found"));
				cartItemResponse.setProductResponse(mapToProductResponse(product.getProductId(), userEntity.getLanguage(), cartItem.getProductId().getId()));
				cartItemResponseList.add(cartItemResponse);
			}
			return cartItemResponseList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void addToCart(CartRequest request) {

		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity = userRepository.findByEmail(email);
			ProductSKUEntity productSKUEntity = productSKURepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
			CartItemEntity cartItem = cartItemRepository.findByProductId(productSKUEntity.getId());
			if (cartItem == null) {
				CartItemEntity cartItemEntity = new CartItemEntity();
				cartItemEntity.setCart(userEntity.getCart());
				cartItemEntity.setQuantity(request.getQuantity());
				cartItemEntity.setProductId(productSKUEntity);
				cartItemEntity.setCart(userEntity.getCart());
				cartItemEntity.setSelected(false);
				cartItemRepository.save(cartItemEntity);
			} else {
				cartItem.setQuantity(request.getQuantity() + cartItem.getQuantity());
				cartItemRepository.save(cartItem);
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void updateCartQuantity(Long id, int quantity) {
		try {
			CartItemEntity cartItemEntity = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item Not Found"));;
			int totalQuantity = cartItemEntity.getQuantity() + quantity;
			cartItemEntity.setQuantity(totalQuantity);
			cartItemEntity.setPrice(cartItemEntity.getProductId().getPrice().multiply(BigDecimal.valueOf(totalQuantity)));
			cartItemRepository.save(cartItemEntity);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {
		try {
			CartItemEntity cartItemEntity = cartItemRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Item Not Found"));
			cartItemRepository.delete(cartItemEntity);
			ShoppingCartEntity shoppingCartEntity = shoppingCartRepository.findById(cartItemEntity.getCart().getId())
					.orElseThrow(() -> new RuntimeException("Item Not Found"));
			shoppingCartRepository.delete(shoppingCartEntity);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	// Hàm ánh xạ ProductEntity sang ProductResponse
	private ProductResponse mapToProductResponse(ProductEntity productEntity, String language, Long productSkuId) {
		ProductResponse productResponse = new ProductResponse();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(productEntity.getId());
		productDTO.setPublished(productEntity.isPublished());
		productDTO.setHotProduct(productEntity.isHotProduct());
		productDTO.setNewProduct(productEntity.isNewProduct());
		productDTO.setImage(productEntity.getImage());
		productDTO.setVideo(productEntity.getVideo());
		productDTO.setUpdateAt(productEntity.getUpdateAt());
		productDTO.setCreateAt(productEntity.getCreateAt());
		productDTO.setDeletedAt(productEntity.getDeletedAt());

		productResponse.setProduct(productDTO);

		// Ánh xạ thông tin bản dịch (translation) theo ngôn ngữ người dùng
		List<ProductTranslationEntity> translations = productTranslantionRepository.findByProductId(productEntity);
		System.out.println("Số lượng bản dịch tìm thấy: " + translations.size());

// Lọc ra bản dịch phù hợp với ngôn ngữ
		Optional<ProductTranslationEntity> translationOpt = translations.stream()
				.filter(productTranslationEntity -> productTranslationEntity.getLanguage().equals(language))
				.findFirst(); // Tìm bản dịch đầu tiên phù hợp

// Nếu có bản dịch, ánh xạ nó sang DTO
		translationOpt.ifPresent(productTranslationEntity -> {
			ProductTranslationDTO translationDTO = new ProductTranslationDTO();
			translationDTO.setId(productTranslationEntity.getId());
			translationDTO.setLanguage(productTranslationEntity.getLanguage());
			translationDTO.setName(productTranslationEntity.getName());
			translationDTO.setDescription(productTranslationEntity.getDescription());
			translationDTO.setCreatedAt(productTranslationEntity.getCreatedAt());
			translationDTO.setUpdatedAt(productTranslationEntity.getUpdatedAt());
			productResponse.setTranslation(translationDTO);
		});
		// Ánh xạ danh sách SKU và variant liên quan
		List<ProductSkuResponse> skuResponses = new ArrayList<>();
		List<ProductSKUEntity> skuEntities = productSKURepository.findSKUsByProductId(productEntity.getId());
		for (ProductSKUEntity skuEntity : skuEntities) {
			if (skuEntity.getId() == productSkuId) {
				ProductSkuResponse skuResponse = new ProductSkuResponse();

				// Ánh xạ thông tin SKU
				ProductSKUDTO skuDTO = new ProductSKUDTO();
				skuDTO.setId(skuEntity.getId());
				skuDTO.setImage(skuEntity.getImage());
				skuDTO.setPrice(skuEntity.getPrice());
				skuDTO.setQuantity(skuEntity.getQuantity());
				skuDTO.setCreatedAt(skuEntity.getCreatedAt());

				skuResponse.setProductSKU(skuDTO);

				// Ánh xạ danh sách variant liên quan
				Map<String, String> attributes = new HashMap<>();
				List<ProductVariantEntity> variantEntities = productVariantRepository.findVariantsBySKUId(skuEntity.getId());
				for (ProductVariantEntity variantEntity : variantEntities) {
					attributes.put(variantEntity.getVariantKey(), variantEntity.getValue()); // Ví dụ: màu sắc
				}

				skuResponse.setVariants(attributes);
				skuResponses.add(skuResponse);
			}

		}

		productResponse.setSkus(skuResponses);
		return productResponse;
	}
}
