package vnskilled.edu.ecom.Service.Impl.Product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vnskilled.edu.ecom.Entity.Categories.CategoriesDescriptionsEntity;
import vnskilled.edu.ecom.Entity.Categories.CategoriesEntity;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;
import vnskilled.edu.ecom.Entity.Products.ProductTranslationEntity;
import vnskilled.edu.ecom.Entity.Products.ProductVariantEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductSKUDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductTranslationDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductVariantDTO;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;
import vnskilled.edu.ecom.Model.Request.Product.ProductRequest;
import vnskilled.edu.ecom.Model.Request.Product.ProductSkuVariantRequest;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;
import vnskilled.edu.ecom.Model.Response.Product.ProductSkuResponse;
import vnskilled.edu.ecom.Repository.Categories.CategoriesDescriptionRepository;
import vnskilled.edu.ecom.Repository.Categories.CategoryRepository;
import vnskilled.edu.ecom.Repository.Product.ProductRepository;
import vnskilled.edu.ecom.Repository.Product.ProductSKURepository;
import vnskilled.edu.ecom.Repository.Product.ProductTranslantionRepository;
import vnskilled.edu.ecom.Repository.Product.ProductVariantRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.FirebaseStorageService;
import vnskilled.edu.ecom.Service.Product.ProductService;
import vnskilled.edu.ecom.Util.MultipartFileImplementation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ProductSKURepository productSKURepository;
    private final ProductTranslantionRepository productTranslantionRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CategoriesDescriptionRepository categoriesDescriptionRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductSKURepository productSKURepository, ProductTranslantionRepository productTranslantionRepository, ProductVariantRepository productVariantRepository, UserRepository userRepository, FirebaseStorageService firebaseStorageService, CategoryRepository categoryRepository, ModelMapper modelMapper, CategoriesDescriptionRepository categoriesDescriptionRepository) {
        this.productRepository = productRepository;
        this.productSKURepository = productSKURepository;
        this.productTranslantionRepository = productTranslantionRepository;
        this.productVariantRepository = productVariantRepository;
        this.userRepository = userRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.categoriesDescriptionRepository = categoriesDescriptionRepository;
    }

    @Override
    public List<ProductResponse> getAllProductsByLanguage() {
        try {
            String lang = "";
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);

            if (userEntity == null) {
                lang = "vi";
            } else {

                lang = userEntity.getLanguage();
            }
            List<ProductEntity> productEntities = productRepository.findAllWithTranslations(lang);

            // Ánh xạ từ ProductEntity sang ProductResponse
            String finalLang = lang;
            return productEntities.stream().filter(productEntity -> productEntity.getDeletedAt() == null).map(productEntity -> mapToProductResponse(productEntity, finalLang)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ProductResponse> getProductByNew() {
        try {
            // Lấy ngữ cảnh người dùng hiện tại
            String lang = "";
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);

            if (userEntity == null) {
                lang = "vi";
            } else {

                lang = userEntity.getLanguage();
            }
            // Lấy danh sách sản phẩm mới (newProduct = true) theo ngôn ngữ người dùng
            List<ProductEntity> productEntities = productRepository.findByNewProductWithTranslations(lang);

            // Ánh xạ từ ProductEntity sang ProductResponse và trả về
            String finalLang = lang;
            return productEntities.stream().filter(productEntity -> productEntity.getDeletedAt() == null).map(productEntity -> mapToProductResponse(productEntity, finalLang)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ProductResponse> getProductByHotLimitTop8() {
        try {
            // Lấy ngữ cảnh người dùng hiện tại
            String lang = "";
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);

            if (userEntity == null) {
                lang = "vi";
            } else {

                lang = userEntity.getLanguage();
            }
            // Tạo đối tượng Pageable với số lượng sản phẩm muốn lấy là 8
            Pageable pageable = PageRequest.of(0, 8);

            // Lấy danh sách 8 sản phẩm hot (hotProduct = true) theo ngôn ngữ người dùng
            List<ProductEntity> productEntities = productRepository.findHotProducts(lang, pageable);

            // Ánh xạ từ ProductEntity sang ProductResponse và trả về
            String finalLang = lang;
            return productEntities.stream().filter(productEntity -> productEntity.getDeletedAt() == null).map(productEntity -> mapToProductResponse(productEntity, finalLang)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void createProduct(ProductRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);
            if (userEntity == null)
                throw new RuntimeException("User Not Found");
            CategoriesEntity categoriesEntity = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category Not Found"));
            // Tạo ProductEntity
            ProductEntity product = new ProductEntity();
            product.setPublished(request.isPublished());
            product.setHotProduct(request.isHotProduct());
            product.setNewProduct(request.isNewProduct());
            product.setCategoryId(categoriesEntity);
            product.setCreateAt(Timestamp.from(Instant.now()));
            product.setUpdateAt(null);
            product.setDeletedAt(null);
            product.setCreateBy(userEntity);
            product.setUpdateBy(null);

            // Tạo JSON cho hình ảnh và video
            try {
                // Tạo đối tượng JSON cho hình ảnh
                List<String> imageList = new ArrayList<>();
                List<String> videoList = new ArrayList<>();

                // Lưu danh sách hình ảnh
                if (request.getImagelist() != null) {
                    for (MultipartFile imageFile : request.getImagelist()) {
                        String imageUrl = firebaseStorageService.uploadFile(imageFile);
                        imageList.add(imageUrl);
                    }
                }

                // Lưu danh sách video
                if (request.getVideolist() != null) {
                    for (MultipartFile videoFile : request.getVideolist()) {
                        String videoUrl = firebaseStorageService.uploadFile(videoFile);
                        videoList.add(videoUrl);
                    }
                }

                // Chuyển đổi danh sách media thành JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String productImage = objectMapper.writeValueAsString(imageList);
                String productVideo = objectMapper.writeValueAsString(videoList);
                product.setImage(productImage); // Lưu vào trường mediaUrls
                product.setVideo(productVideo); // Lưu vào trường mediaUrls
            } catch (JsonProcessingException e) {
                // Xử lý lỗi khi chuyển đổi thành JSON
                e.printStackTrace();
                throw new RuntimeException("Error creating JSON for media URLs: " + e.getMessage());
            }

            // Lưu sản phẩm vào cơ sở dữ liệu
            ProductEntity savedProduct = productRepository.save(product);

            // Tạo bản dịch
            ProductTranslationEntity translation = new ProductTranslationEntity();
            translation.setLanguage(request.getLanguage());
            translation.setName(request.getName());
            translation.setDescription(request.getDescription());
            translation.setProductId(savedProduct); // Thiết lập mối quan hệ
            translation.setCreatedAt(Timestamp.from(Instant.now()));
            productTranslantionRepository.save(translation);

            // Tạo SKU
            ProductSKUEntity sku = new ProductSKUEntity();
            sku.setImage(request.getImage() != null ? firebaseStorageService.uploadFile(request.getImage()) : null);
            sku.setPrice(request.getPrice());
            sku.setQuantity(request.getQuantity());
            sku.setProductId(savedProduct); // Thiết lập mối quan hệ
            sku.setCreatedAt(Timestamp.from(Instant.now()));
            sku.setCreateBy(userEntity);
            ProductSKUEntity savedSKU = productSKURepository.save(sku);

            // Tạo Variant
            ProductVariantEntity variant = new ProductVariantEntity();
            variant.setVariantKey(request.getVariantKey());
            variant.setValue(request.getValue());
            variant.setVariantId(savedSKU); // Thiết lập mối quan hệ
            variant.setCreatedAt(Timestamp.from(Instant.now()));
            variant.setLanguage(request.getLanguage());
            productVariantRepository.save(variant);

            // Tạo và trả về ProductResponse
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ProductResponse getProductById(Long id) {
        try {
            String lang = "";
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);

            if (userEntity == null) {
                lang = "vi";
            } else {

                lang = userEntity.getLanguage();
            }
            ProductEntity product = productRepository.findByIdWithTranslations(lang, id);

            // Ánh xạ từ ProductEntity sang ProductResponse
            return mapToProductResponse(product, lang);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateProduct(ProductDTO request) {
        try {
            CategoriesEntity categories = categoryRepository.findById(request.getCategoryId().getId()).orElseThrow(() -> new RuntimeException("Category Not Found"));
            ProductEntity productEntity = productRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
            productEntity.setUpdateAt(Timestamp.from(Instant.now()));
            productEntity.setHotProduct(request.isHotProduct());
            productEntity.setNewProduct(request.isNewProduct());
            productEntity.setImage(request.getImage());
            productEntity.setVideo(request.getVideo());
            productEntity.setCategoryId(categories);
            productEntity.setPublished(request.isPublished());
            productRepository.save(productEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            ProductEntity product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
            product.setDeletedAt(Timestamp.from(Instant.now()));
            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void updateTranslation(Long id, ProductTranslationDTO translation) {
        try {
            ProductEntity entity = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));

            List<ProductTranslationEntity> productTranslationEntity = productTranslantionRepository.findByProductId(entity);
//			for (ProductTranslationEntity translationEntity : productTranslationEntity) {
//				if (translationEntity.getLanguage ().equals (translation.getLanguage ())) {
//					throw new RuntimeException ("Bản dịch với ngôn ngữ này đã có");
//				}
//			}
            ProductTranslationEntity productTranslation = productTranslantionRepository.findById(translation.getId()).orElseThrow(() -> new RuntimeException("Translation Not Found"));
            productTranslation.setLanguage(translation.getLanguage());
            productTranslation.setName(translation.getName());
            productTranslation.setDescription(translation.getDescription());
            productTranslation.setUpdatedAt(Timestamp.from(Instant.now()));
            productTranslantionRepository.save(productTranslation);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void createTranslation(ProductTranslationDTO translation) {
        try {
            ProductEntity entity = productRepository.findById(translation.getProductId().getId()).orElseThrow(() -> new RuntimeException("Product Not Found"));

            List<ProductTranslationEntity> productTranslationEntity = productTranslantionRepository.findByProductId(entity);
            for (ProductTranslationEntity translationEntity : productTranslationEntity) {
                if (translationEntity.getLanguage().equals(translation.getLanguage())) {
                    throw new RuntimeException("Bản dịch với ngôn ngữ này đã có");
                }
            }
            ProductTranslationEntity productTranslation = new ProductTranslationEntity();
            productTranslation.setLanguage(translation.getLanguage());
            productTranslation.setName(translation.getName());
            productTranslation.setDescription(translation.getDescription());
            productTranslation.setCreatedAt(Timestamp.from(Instant.now()));
            productTranslation.setProductId(entity);
            productTranslation.setUpdatedAt(null);
            productTranslantionRepository.save(productTranslation);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteTranslation(Long id) {

    }

    @Override
    public void createSku(ProductSkuVariantRequest request) {
        try {
            ProductEntity entity = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);
            if (userEntity == null)
                throw new RuntimeException("User Not Found");
            ProductSKUEntity productSKUEntity = new ProductSKUEntity();
            productSKUEntity.setProductId(entity);
            productSKUEntity.setCreateBy(userEntity);
            productSKUEntity.setUpdateBy(null);
            productSKUEntity.setUpdatedAt(null);
            productSKUEntity.setCreatedAt(Timestamp.from(Instant.now()));
            productSKUEntity.setPrice(request.getPrice());
            productSKUEntity.setQuantity(request.getQuantity());
            productSKUEntity.setImage(request.getImage());
            ProductSKUEntity saveSku = productSKURepository.save(productSKUEntity);

            ProductVariantEntity variant = new ProductVariantEntity();
            variant.setLanguage(request.getLang());
            variant.setCreatedAt(Timestamp.from(Instant.now()));
            variant.setVariantId(saveSku);
            variant.setVariantKey(request.getVariantKey());
            variant.setValue(request.getValue());
            productVariantRepository.save(variant);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteSku(Long id) {

    }

    @Override
    public void updateSku(Long id, ProductSkuVariantRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);
            if (userEntity == null)
                throw new RuntimeException("User Not Found");
            ProductSKUEntity productSKUEntity = productSKURepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
            List<ProductVariantEntity> variantEntities = productVariantRepository.findVariantsBySKUId(id);
            ProductVariantEntity variant = variantEntities.get(0);
            productSKUEntity.setImage(request.getImage());
            productSKUEntity.setUpdatedAt(Timestamp.from(Instant.now()));
            productSKUEntity.setQuantity(request.getQuantity());
            productSKUEntity.setQuantity(request.getQuantity());
            productSKUEntity.setPrice(request.getPrice());
            productSKUEntity.setUpdateBy(userEntity);
            ProductSKUEntity saveSku = productSKURepository.save(productSKUEntity);

            variant.setUpdatedAt(Timestamp.from(Instant.now()));
            variant.setVariantId(saveSku);
            variant.setVariantKey(request.getVariantKey());
            variant.setValue(request.getValue());
            variant.setLanguage(request.getLang());

            productVariantRepository.save(variant);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createVariant(ProductVariantDTO productVariant) {
        try {
            ProductSKUEntity sku = productSKURepository.findById(productVariant.getVariantId().getId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
            List<ProductVariantEntity> productVariantEntities = productVariantRepository.findVariantsBySKUId(productVariant.getVariantId().getId());
            for (ProductVariantEntity productVariantEntity : productVariantEntities) {
                if (productVariantEntity.getLang().equals(productVariant.getLang())) {
                    throw new RuntimeException("Biến thể có mã ngôn ngữ là " + productVariant.getLang() + " đã có vui lòng nhập thông tin với ngôn ngữ khác");
                }
            }
            ProductVariantEntity entity = new ProductVariantEntity();
            entity.setLanguage(productVariant.getLanguage());
            entity.setLang(productVariant.getLanguage());
            entity.setCreatedAt(Timestamp.from(Instant.now()));
            entity.setVariantKey(productVariant.getVariantKey());
            entity.setValue(productVariant.getValue());
            entity.setVariantId(sku);
            productVariantRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteVariant(Long id) {
        try {
            ProductVariantEntity entity = productVariantRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
            productVariantRepository.delete(entity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateVariant(ProductVariantDTO productVariant) {
        try {
            ProductSKUEntity sku = productSKURepository.findById(productVariant.getVariantId().getId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
            ProductVariantEntity entity = productVariantRepository.findById(productVariant.getId()).orElseThrow(() -> new RuntimeException("Product Not Found"));
            entity.setLanguage(productVariant.getLanguage());
            entity.setLang(productVariant.getLanguage());
            entity.setUpdatedAt(Timestamp.from(Instant.now()));
            entity.setVariantKey(productVariant.getVariantKey());
            entity.setValue(productVariant.getValue());
            entity.setVariantId(sku);
            productVariantRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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
        productDTO.setUpdateAt(productEntity.getUpdateAt());
        productDTO.setCreateAt(productEntity.getCreateAt());
        productDTO.setDeletedAt(productEntity.getDeletedAt());

        productResponse.setProduct(productDTO);

        // Ánh xạ thông tin bản dịch (translation) theo ngôn ngữ người dùng
        List<ProductTranslationEntity> translations = productTranslantionRepository.findByProductId(productEntity);
        System.out.println("Số lượng bản dịch tìm thấy: " + translations.size());

// Lọc ra bản dịch phù hợp với ngôn ngữ
        Optional<ProductTranslationEntity> translationOpt = translations.stream().filter(productTranslationEntity -> productTranslationEntity.getLanguage().equals(language)).findFirst(); // Tìm bản dịch đầu tiên phù hợp

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

        productResponse.setSkus(skuResponses);
        return productResponse;
    }

    @Override
    public void imPortProduct(ProductRequest request) {
        try {
            // Lấy thông tin người dùng và danh mục
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity= userRepository.findByEmail (email);
            if (userEntity == null)
                throw new RuntimeException("User Not Found");
            CategoriesEntity categoriesEntity = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category Not Found"));

            // Kiểm tra sản phẩm đã tồn tại chưa
            Optional<ProductEntity> existingProductOpt = productRepository.findByNameAndCategoryId(request.getName(), request.getCategoryId());

            ProductEntity product;
            if (existingProductOpt.isPresent()) {
                // Sản phẩm đã tồn tại, kiểm tra biến thể
                product = existingProductOpt.get();

                Optional<ProductSKUEntity> existingSKUOpt = productSKURepository.findByProductIdAndVariantKeyAndValue(product.getId(), request.getVariantKey(), request.getValue());

                if (existingSKUOpt.isPresent()) {
                    // Biến thể đã tồn tại, cộng thêm số lượng
                    ProductSKUEntity existingSKU = existingSKUOpt.get();
                    existingSKU.setQuantity(existingSKU.getQuantity() + request.getQuantity());
                    existingSKU.setUpdatedAt(Timestamp.from(Instant.now()));
                    existingSKU.setUpdateBy(userEntity);
                    productSKURepository.save(existingSKU);
                } else {
                    // Biến thể chưa tồn tại, tạo mới biến thể
                    createNewVariantAndSKU(product, request, userEntity);
                }
            } else {
                // Sản phẩm chưa tồn tại, tạo mới sản phẩm
                product = createNewProduct(request, categoriesEntity, userEntity);
                createNewVariantAndSKU(product, request, userEntity);
            }

            // Tạo hoặc cập nhật bản dịch sản phẩm
            createOrUpdateProductTranslation(product, request);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ProductEntity createNewProduct(ProductRequest request, CategoriesEntity category, UserEntity user) {
        ProductEntity product = new ProductEntity();
        product.setPublished(request.isPublished());
        product.setHotProduct(request.isHotProduct());
        product.setNewProduct(request.isNewProduct());
        product.setCategoryId(category);
        product.setCreateAt(Timestamp.from(Instant.now()));
        product.setCreateBy(user);

        // Lưu thông tin hình ảnh và video
        saveProductMedia(product, request);

        return productRepository.save(product);
    }

    // Tạo mới biến thể và SKU
    private void createNewVariantAndSKU(ProductEntity product, ProductRequest request, UserEntity user) {
        // Tạo SKU
        ProductSKUEntity sku = new ProductSKUEntity();
        try {
            sku.setImage(request.getImage() != null ? firebaseStorageService.uploadFile(request.getImage()) : null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sku.setPrice(request.getPrice());
        sku.setQuantity(request.getQuantity());
        sku.setProductId(product);
        sku.setCreatedAt(Timestamp.from(Instant.now()));
        sku.setCreateBy(user);
        ProductSKUEntity savedSKU = productSKURepository.save(sku);

        // Tạo Variant
        ProductVariantEntity variant = new ProductVariantEntity();
        variant.setVariantKey(request.getVariantKey());
        variant.setValue(request.getValue());
        variant.setVariantId(savedSKU);
        variant.setCreatedAt(Timestamp.from(Instant.now()));
        variant.setLanguage(request.getLanguage());
        productVariantRepository.save(variant);
    }

    // Tạo hoặc cập nhật bản dịch sản phẩm
    private void createOrUpdateProductTranslation(ProductEntity product, ProductRequest request) {
        Optional<ProductTranslationEntity> translationOpt = productTranslantionRepository.findByProductIdAndLanguage(product, request.getLanguage());

        ProductTranslationEntity translation;
        if (translationOpt.isPresent()) {
            translation = translationOpt.get();
            translation.setName(request.getName());
            translation.setDescription(request.getDescription());
            translation.setUpdatedAt(Timestamp.from(Instant.now()));
        } else {
            translation = new ProductTranslationEntity();
            translation.setLanguage(request.getLanguage());
            translation.setName(request.getName());
            translation.setDescription(request.getDescription());
            translation.setProductId(product);
            translation.setCreatedAt(Timestamp.from(Instant.now()));
        }
        productTranslantionRepository.save(translation);
    }

    // Lưu thông tin hình ảnh và video
    private void saveProductMedia(ProductEntity product, ProductRequest request) {
        List<String> imageList = new ArrayList<>();
        List<String> videoList = new ArrayList<>();

        try {
            if (request.getImagelist() != null) {
                for (MultipartFile imageFile : request.getImagelist()) {
                    String imageUrl = firebaseStorageService.uploadFile(imageFile);
                    imageList.add(imageUrl);
                }
            }

            if (request.getVideolist() != null) {
                for (MultipartFile videoFile : request.getVideolist()) {
                    String videoUrl = firebaseStorageService.uploadFile(videoFile);
                    videoList.add(videoUrl);
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            product.setImage(objectMapper.writeValueAsString(imageList));
            product.setVideo(objectMapper.writeValueAsString(videoList));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error creating JSON for media URLs: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String exportProductToExcelAndUpload() throws IOException {
        try {
            // Tạo workbook và sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            var sheet = workbook.createSheet("Products");

            // Tiêu đề cột
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Product ID");
            header.createCell(1).setCellValue("Product Name");
            header.createCell(2).setCellValue("Category Name");
            header.createCell(3).setCellValue("Price");
            header.createCell(4).setCellValue("Quantity");
            header.createCell(5).setCellValue("Published");
            header.createCell(6).setCellValue("Hot Product");
            header.createCell(7).setCellValue("New Product");
            header.createCell(8).setCellValue("SKU Image");
            header.createCell(9).setCellValue("Variant Key");
            header.createCell(10).setCellValue("Variant Value");
            header.createCell(11).setCellValue("Translation Language");
            header.createCell(12).setCellValue("Description");
            header.createCell(13).setCellValue("Created At");
            header.createCell(14).setCellValue("Updated At");
            header.createCell(15).setCellValue("Created By");
            header.createCell(16).setCellValue("Updated By");

            int rowNum = 1;

            // Lấy danh sách sản phẩm
            List<ProductEntity> products = productRepository.findAll();

            for (ProductEntity product : products) {
                // Lấy danh sách SKU của sản phẩm
                List<ProductSKUEntity> skus = productSKURepository.findSKUsByProductId(product.getId());
                List<CategoriesDescriptionsEntity> categoriesDescriptionsEntity = categoriesDescriptionRepository.findByCategoryId(product.getCategoryId());
                String categoryName = "";
                if (categoriesDescriptionsEntity != null && !categoriesDescriptionsEntity.isEmpty()) {
                    categoryName = categoriesDescriptionsEntity.get(0).getName(); // Lấy tên từ phần tử đầu tiên
                } else {
                    categoryName = ""; // Thiết lập tên danh mục là rỗng nếu không có dữ liệu
                }

                // Lấy danh sách bản dịch của sản phẩm
                List<ProductTranslationEntity> translations = productTranslantionRepository.findByProductId(product);

                for (ProductSKUEntity sku : skus) {
                    // Lấy danh sách các biến thể dựa trên SKU
                    List<ProductVariantEntity> variants = productVariantRepository.findVariantsBySKUId(sku.getId());

                    for (ProductTranslationEntity translation : translations) {
                        if (variants.isEmpty()) {
                            Row row = sheet.createRow(rowNum++);

                            // Điền dữ liệu vào từng cột
                            row.createCell(0).setCellValue(product.getId()); // Product ID
                            row.createCell(1).setCellValue(translation != null ? translation.getName() : ""); // Product Name
                            row.createCell(2).setCellValue(categoryName); // Category Name
                            row.createCell(3).setCellValue(sku.getPrice() != null ? sku.getPrice().doubleValue() : 0); // Price
                            row.createCell(4).setCellValue(sku.getQuantity()); // Quantity
                            row.createCell(5).setCellValue(product.isPublished() ? "Yes" : "No"); // Published
                            row.createCell(6).setCellValue(product.isHotProduct() ? "Yes" : "No"); // Hot Product
                            row.createCell(7).setCellValue(product.isNewProduct() ? "Yes" : "No"); // New Product
                            row.createCell(8).setCellValue(sku.getImage() != null ? sku.getImage() : ""); // SKU Image
                            row.createCell(9).setCellValue(""); // Variant Key (No Variant)
                            row.createCell(10).setCellValue(""); // Variant Value (No Variant
                            row.createCell(11).setCellValue(translation != null ? translation.getLanguage() : ""); // Translation Language
                            row.createCell(12).setCellValue(translation != null ? translation.getDescription() : ""); // Description
                            row.createCell(13).setCellValue(product.getCreateAt() != null ? product.getCreateAt().toString() : ""); // Created At
                            row.createCell(14).setCellValue(product.getUpdateAt() != null ? product.getUpdateAt().toString() : ""); // Created At
                            row.createCell(15).setCellValue(product.getCreateBy() != null ? product.getCreateBy().getFirstName() + " " + product.getCreateBy().getLastName() : ""); // Created At
                            row.createCell(16).setCellValue(product.getUpdateBy() != null ? product.getUpdateBy().getFirstName() + " " + product.getUpdateBy().getLastName() : ""); // Created At
                        } else {
                            // Xuất thông tin với từng biến thể
                            for (ProductVariantEntity variant : variants) {
                                Row row = sheet.createRow(rowNum++);

                                // Điền dữ liệu vào từng cột
                                row.createCell(0).setCellValue(product.getId()); // Product ID
                                row.createCell(1).setCellValue(translation != null ? translation.getName() : ""); // Product Name
                                row.createCell(2).setCellValue(categoryName); // Category Name
                                row.createCell(3).setCellValue(sku.getPrice() != null ? sku.getPrice().doubleValue() : 0); // Price
                                row.createCell(4).setCellValue(sku.getQuantity()); // Quantity
                                row.createCell(5).setCellValue(product.isPublished() ? "Yes" : "No"); // Published
                                row.createCell(6).setCellValue(product.isHotProduct() ? "Yes" : "No"); // Hot Product
                                row.createCell(7).setCellValue(product.isNewProduct() ? "Yes" : "No"); // New Product
                                row.createCell(8).setCellValue(sku.getImage() != null ? sku.getImage() : ""); // SKU Image
                                row.createCell(9).setCellValue(variant.getVariantKey() != null ? variant.getVariantKey() : ""); // Variant Key
                                row.createCell(10).setCellValue(variant.getValue() != null ? variant.getValue() : ""); // Variant Value
                                row.createCell(11).setCellValue(translation != null ? translation.getLanguage() : ""); // Translation Language
                                row.createCell(12).setCellValue(translation != null ? translation.getDescription() : ""); // Description
                                row.createCell(13).setCellValue(product.getCreateAt() != null ? product.getCreateAt().toString() : ""); // Created At
                                row.createCell(14).setCellValue(product.getUpdateAt() != null ? product.getUpdateAt().toString() : ""); // Created At
                                row.createCell(15).setCellValue(product.getCreateBy() != null ? product.getCreateBy().getFirstName() + " " + product.getCreateBy().getLastName() : ""); // Created At
                                row.createCell(16).setCellValue(product.getUpdateBy() != null ? product.getUpdateBy().getFirstName() + " " + product.getUpdateBy().getLastName() : ""); // Created At

                            }
                        }
                    }
                }
            }

            // Lưu file vào byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            byte[] bytes = outputStream.toByteArray();

            // Tải lên Firebase và nhận URL
            String fileName = "exported_products.xlsx";
            String downloadUrl = firebaseStorageService.uploadFile(new MultipartFileImplementation(bytes, fileName));

            // Trả về đường dẫn tải về
            return downloadUrl;

        } catch (IOException e) {
            throw new RuntimeException("Error while exporting products: " + e.getMessage());
        }
    }
}
