package vnskilled.edu.ecom.Service.Impl.Favorites;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Favorites.FavoritesEntity;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;
import vnskilled.edu.ecom.Entity.Products.ProductTranslationEntity;
import vnskilled.edu.ecom.Entity.Products.ProductVariantEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.Favorites.FavoritesDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductSKUDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductTranslationDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Response.Favorites.FavoriteResponse;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;
import vnskilled.edu.ecom.Model.Response.Product.ProductSkuResponse;
import vnskilled.edu.ecom.Repository.Favorites.FavoriteRepository;
import vnskilled.edu.ecom.Repository.Product.ProductRepository;
import vnskilled.edu.ecom.Repository.Product.ProductSKURepository;
import vnskilled.edu.ecom.Repository.Product.ProductTranslantionRepository;
import vnskilled.edu.ecom.Repository.Product.ProductVariantRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.Favorites.FavoriteService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class FavoriteServiceImpl implements FavoriteService {
	private FavoriteRepository favoriteRepository;
	private UserRepository userRepository;
	private ProductRepository productRepository;
	private ProductSKURepository productSKURepository;
	private ProductVariantRepository productVariantRepository;
	private ProductTranslantionRepository productTranslantionRepository;
	private final ModelMapper modelMapper;
	@Autowired
	public FavoriteServiceImpl (FavoriteRepository favoriteRepository, UserRepository userRepository, ProductRepository productRepository, ProductSKURepository productSKURepository, ProductVariantRepository productVariantRepository, ProductTranslantionRepository productTranslantionRepository, ModelMapper modelMapper) {
		this.favoriteRepository = favoriteRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.productSKURepository = productSKURepository;
		this.productVariantRepository = productVariantRepository;
		this.productTranslantionRepository = productTranslantionRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<FavoriteResponse> getByUserId () {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity= userRepository.findByEmail (email);
			if (userEntity == null)
				throw new RuntimeException("User Not Found");
			List<FavoriteResponse> list = new ArrayList<> ();
			List<FavoritesEntity> favoritesEntities = favoriteRepository.findByUserId (userEntity);
			for (FavoritesEntity favoritesEntity : favoritesEntities) {
				FavoriteResponse  response = new FavoriteResponse ();
				ProductResponse productResponse = mapToProductResponse (favoritesEntity.getProductId (), userEntity.getLanguage ());
				FavoritesDTO favoritesDTO = modelMapper.map (favoritesEntity, FavoritesDTO.class);
				response.setFavorites (favoritesDTO);
				response.setProduct (productResponse);
				list.add (response);
			}
			return list;
		}catch (Exception e){
			throw new RuntimeException (e.getMessage ());
		}
	}

	@Override
	public void addFavorite (Long productId) {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity user = userRepository.findByEmail (email);
			if (user == null)
				throw new RuntimeException("User Not Found");
			ProductEntity product = productRepository.findById (productId).orElseThrow (() -> new NoSuchElementException ("Product Not Found"));
			FavoritesEntity favorites = new FavoritesEntity ();
			favorites.setUserId (user);
			favorites.setProductId (product);
			favorites.setCreatedAt (Timestamp.from (Instant.now ()));
			favorites.setUpdatedAt (null);
			favoriteRepository.save(favorites);
		} catch (Exception e) {
			throw new RuntimeException (e.getMessage ());
		}

	}

	@Override
	public void deleteFavorite (Long id) {
		try {
			FavoritesEntity favorites = favoriteRepository.findById (id).orElseThrow (()-> new NoSuchElementException ("Favorite Not Found"));
			favoriteRepository.delete (favorites);
		}catch (Exception e){
			throw new RuntimeException (e.getMessage ());
		}

	}

	// Hàm ánh xạ ProductEntity sang ProductResponse
	private ProductResponse mapToProductResponse(ProductEntity productEntity, String language) {
		ProductResponse productResponse = new ProductResponse();

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(productEntity.getId());
		productDTO.setPublished(productEntity.isPublished());
		productDTO.setHotProduct(productEntity.isHotProduct());
		productDTO.setNewProduct(productEntity.isNewProduct());
		productDTO.setImage(productEntity.getImage());
		productDTO.setVideo(productEntity.getVideo());
		productDTO.setUpdateAt (productEntity.getUpdateAt ());
		productDTO.setCreateAt (productEntity.getCreateAt ());
		productDTO.setDeletedAt (productEntity.getDeletedAt ());

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
			translationDTO.setCreatedAt (productTranslationEntity.getCreatedAt());
			translationDTO.setUpdatedAt (productTranslationEntity.getUpdatedAt());
			productResponse.setTranslation(translationDTO);
		});
		// Ánh xạ danh sách SKU và variant liên quan
		List<ProductSkuResponse> skuResponses = new ArrayList<> ();
		List<ProductSKUEntity> skuEntities = productSKURepository.findSKUsByProductId(productEntity.getId());
		for (ProductSKUEntity skuEntity : skuEntities) {
			ProductSkuResponse skuResponse = new ProductSkuResponse();

			// Ánh xạ thông tin SKU
			ProductSKUDTO skuDTO = new ProductSKUDTO();
			skuDTO.setId(skuEntity.getId());
			skuDTO.setImage(skuEntity.getImage());
			skuDTO.setPrice(skuEntity.getPrice());
			skuDTO.setQuantity(skuEntity.getQuantity());
			skuDTO.setCreatedAt (skuEntity.getCreatedAt());

			skuResponse.setProductSKU(skuDTO);

			// Ánh xạ danh sách variant liên quan
			Map<String, String> attributes = new HashMap<> ();
			List<ProductVariantEntity> variantEntities = productVariantRepository.findVariantsBySKUId(skuEntity.getId());
			for (ProductVariantEntity variantEntity : variantEntities) {
				attributes.put(variantEntity.getVariantKey (), variantEntity.getValue ()); // Ví dụ: màu sắc
			}

			skuResponse.setVariants (attributes);
			skuResponses.add(skuResponse);
		}

		productResponse.setSkus(skuResponses);
		return productResponse;
	}
}
