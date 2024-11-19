package vnskilled.edu.ecom.Service.Impl.Order;

import com.google.api.gax.rpc.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Orders.OrderDetailEntity;
import vnskilled.edu.ecom.Entity.Orders.OrderEntity;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;
import vnskilled.edu.ecom.Entity.Products.ProductTranslationEntity;
import vnskilled.edu.ecom.Entity.Products.ProductVariantEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Entity.Vouchers.VouchersEntity;
import vnskilled.edu.ecom.Model.DTO.Orders.OrderDTO;
import vnskilled.edu.ecom.Model.DTO.Orders.OrderDetailDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductSKUDTO;
import vnskilled.edu.ecom.Model.DTO.Products.ProductTranslationDTO;
import vnskilled.edu.ecom.Model.Request.Order.OrderRequest;
import vnskilled.edu.ecom.Model.Request.Order.ProductOrderRequest;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Response.Order.OrderDetailResponse;
import vnskilled.edu.ecom.Model.Response.Order.OrderResponse;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;
import vnskilled.edu.ecom.Model.Response.Product.ProductSkuResponse;
import vnskilled.edu.ecom.Repository.Address.AddressCityRepository;
import vnskilled.edu.ecom.Repository.Address.AddressCountryRepository;
import vnskilled.edu.ecom.Repository.Address.AddressWardsRepository;
import vnskilled.edu.ecom.Repository.Address.UserAddressRepository;
import vnskilled.edu.ecom.Repository.Order.OrderDetailRepository;
import vnskilled.edu.ecom.Repository.Order.OrderRepository;
import vnskilled.edu.ecom.Repository.Payment.PaymentRepository;
import vnskilled.edu.ecom.Repository.Product.ProductRepository;
import vnskilled.edu.ecom.Repository.Product.ProductSKURepository;
import vnskilled.edu.ecom.Repository.Product.ProductTranslantionRepository;
import vnskilled.edu.ecom.Repository.Product.ProductVariantRepository;
import vnskilled.edu.ecom.Repository.Shipping.ShippingRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Repository.Vouchers.VoucherRepository;
import vnskilled.edu.ecom.Service.Order.OrderService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
	private  UserRepository userRepository;
	private OrderRepository orderRepository;
	private OrderDetailRepository orderDetailRepository;
	private ProductRepository productRepository;
	private ProductSKURepository productSKURepository;
	private ProductTranslantionRepository productTranslantionRepository;
	private ProductVariantRepository productVariantRepository;
	private UserAddressRepository addressRepository;
	private AddressCityRepository addressCityRepository;
	private AddressCountryRepository addressCountryRepository;
	private AddressWardsRepository addressWardsRepository;
	private ShippingRepository shippingRepository;
	private PaymentRepository paymentRepository;
	private VoucherRepository voucherRepository;
	private ModelMapper modelMapper;

	@Autowired
	public OrderServiceImpl (OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository, ProductSKURepository productSKURepository, ProductTranslantionRepository productTranslantionRepository, ProductVariantRepository productVariantRepository, UserAddressRepository addressRepository, AddressCityRepository addressCityRepository, AddressCountryRepository addressCountryRepository, AddressWardsRepository addressWardsRepository, ShippingRepository shippingRepository, PaymentRepository paymentRepository, VoucherRepository voucherRepository, ModelMapper modelMapper, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.orderDetailRepository = orderDetailRepository;
		this.productRepository = productRepository;
		this.productSKURepository = productSKURepository;
		this.productTranslantionRepository = productTranslantionRepository;
		this.productVariantRepository = productVariantRepository;
		this.addressRepository = addressRepository;
		this.addressCityRepository = addressCityRepository;
		this.addressCountryRepository = addressCountryRepository;
		this.addressWardsRepository = addressWardsRepository;
		this.shippingRepository = shippingRepository;
		this.paymentRepository = paymentRepository;
		this.voucherRepository = voucherRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<OrderResponse> getAllOrders (Pageable pageable) {
		try {
			List<OrderResponse> orderResponses = new ArrayList<>();
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity= userRepository.findByEmail (email);
			if (userEntity == null)
				throw new RuntimeException("User Not Found");
			// Lấy tất cả các đơn hàng
			List<OrderEntity> orders = orderRepository.findAll(pageable).getContent();

			for (OrderEntity order : orders) {
				OrderResponse orderResponse = new OrderResponse();

				// Ánh xạ OrderDTO
				OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
				orderResponse.setOrder(orderDTO);

				// Lấy chi tiết đơn hàng (order details)
				List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId(order);
				List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

				for (OrderDetailEntity orderDetail : orderDetails) {
					OrderDetailResponse detailResponse = new OrderDetailResponse();

					// Ánh xạ OrderDetailDTO
					OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
					detailResponse.setOrderDetail(orderDetailDTO);

					// Ánh xạ sản phẩm liên quan
					ProductEntity product = productRepository.findById(orderDetail.getProductId().getId())
							.orElseThrow(() -> new RuntimeException("Product Not Found"));
					ProductResponse productResponse = mapToProductResponse(product, userEntity.getLanguage(), orderDetail.getProductId().getId());

					detailResponse.setProduct(productResponse);
					orderDetailResponses.add(detailResponse);
				}

				orderResponse.setOrderDetail(orderDetailResponses);
				orderResponses.add(orderResponse);
			}
			return orderResponses;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<OrderResponse> getByUserId (Pageable pageable) {
		try {
			List<OrderResponse> orderResponses = new ArrayList<>();
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity= userRepository.findByEmail (email);
			if (userEntity == null)
				throw new RuntimeException("User Not Found");	// Lấy tất cả các đơn hàng
			List<OrderEntity> orders = orderRepository.findByUserId (userEntity,pageable);
			if (orders.isEmpty()) {
				throw new NoSuchElementException ("No Orders Found");
			}

			for (OrderEntity order : orders) {
				OrderResponse orderResponse = new OrderResponse();

				// Ánh xạ OrderDTO
				OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
				orderResponse.setOrder(orderDTO);

				// Lấy chi tiết đơn hàng (order details)
				List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId(order);
				List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

				for (OrderDetailEntity orderDetail : orderDetails) {
					OrderDetailResponse detailResponse = new OrderDetailResponse();

					// Ánh xạ OrderDetailDTO
					OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
					detailResponse.setOrderDetail(orderDetailDTO);

					// Ánh xạ sản phẩm liên quan
					ProductEntity product = productRepository.findById(orderDetail.getProductId().getId())
							.orElseThrow(() -> new RuntimeException("Product Not Found"));
					ProductResponse productResponse = mapToProductResponse(product, userEntity.getLanguage(), orderDetail.getProductId().getId());

					detailResponse.setProduct(productResponse);
					orderDetailResponses.add(detailResponse);
				}

				orderResponse.setOrderDetail(orderDetailResponses);
				orderResponses.add(orderResponse);
			}
			return orderResponses;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public OrderResponse getOrderById (Long id) {
		try {

			OrderEntity orders = orderRepository.findById (id).orElse (null);


				OrderResponse orderResponse = new OrderResponse();
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity= userRepository.findByEmail (email);
			if (userEntity == null)
				throw new RuntimeException("User Not Found");
			// Ánh xạ OrderDTO
				OrderDTO orderDTO = modelMapper.map(orders, OrderDTO.class);
				orderResponse.setOrder(orderDTO);

				// Lấy chi tiết đơn hàng (order details)
				List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId(orders);
				List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

				for (OrderDetailEntity orderDetail : orderDetails) {
					OrderDetailResponse detailResponse = new OrderDetailResponse();

					// Ánh xạ OrderDetailDTO
					OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
					detailResponse.setOrderDetail(orderDetailDTO);

					// Ánh xạ sản phẩm liên quan
					ProductEntity product = productRepository.findById(orderDetail.getProductId().getId())
							.orElseThrow(() -> new RuntimeException("Product Not Found"));
					ProductResponse productResponse = mapToProductResponse(product, userEntity.getLanguage(), orderDetail.getProductId().getId());

					detailResponse.setProduct(productResponse);
					orderDetailResponses.add(detailResponse);
				}

				orderResponse.setOrderDetail(orderDetailResponses);
			return orderResponse;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<OrderResponse> getOrdersByStatus (String status,Pageable pageable) {
		try {
			List<OrderResponse> orderResponses = new ArrayList<>();
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity= userRepository.findByEmail (email);
			if (userEntity == null)
				throw new RuntimeException("User Not Found");
			// Lấy tất cả các đơn hàng
			List<OrderEntity> orders = orderRepository.findByStatus (status,pageable);
			if (orders.isEmpty())
				throw new NoSuchElementException ("No Orders Found");

			for (OrderEntity order : orders) {
				OrderResponse orderResponse = new OrderResponse();

				// Ánh xạ OrderDTO
				OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
				orderResponse.setOrder(orderDTO);

				// Lấy chi tiết đơn hàng (order details)
				List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId(order);
				List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

				for (OrderDetailEntity orderDetail : orderDetails) {
					OrderDetailResponse detailResponse = new OrderDetailResponse();

					// Ánh xạ OrderDetailDTO
					OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
					detailResponse.setOrderDetail(orderDetailDTO);

					// Ánh xạ sản phẩm liên quan
					ProductEntity product = productRepository.findById(orderDetail.getProductId().getId())
							.orElseThrow(() -> new RuntimeException("Product Not Found"));
					ProductResponse productResponse = mapToProductResponse(product, userEntity.getLanguage(), orderDetail.getProductId().getId());

					detailResponse.setProduct(productResponse);
					orderDetailResponses.add(detailResponse);
				}

				orderResponse.setOrderDetail(orderDetailResponses);
				orderResponses.add(orderResponse);
			}
			return orderResponses;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void placeOrder (OrderRequest orderRequest) {
		try {
			// 1. Lấy thông tin người dùng từ context
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserEntity userEntity= userRepository.findByEmail (email);
			if (userEntity == null)
				throw new RuntimeException("User Not Found");
			// 2. Tính tổng giá trị đơn hàng
			BigDecimal totalPrice = BigDecimal.ZERO;
			for (ProductOrderRequest productRequest : orderRequest.getProducts()) {
				ProductSKUEntity productSKU = productSKURepository.findById(productRequest.getProductId())
						.orElseThrow(() -> new RuntimeException("Product Not Found"));

				BigDecimal productPrice = productSKU.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
				totalPrice = totalPrice.add(productPrice);
			}

			// 3. Áp dụng voucher (nếu có)
			if (orderRequest.getVoucherId() != null) {
				VouchersEntity voucher = voucherRepository.findById(orderRequest.getVoucherId())
						.orElseThrow(() -> new RuntimeException("Voucher Not Found"));
				totalPrice = totalPrice.subtract(voucher.getDiscountValue ());
			}

			// 4. Tạo đối tượng Order
			OrderEntity order = new OrderEntity();
			order.setUserId (userEntity);
			order.setTotalPrice(totalPrice);
			order.setAddress(orderRequest.getAddress());
			order.setPhone(orderRequest.getPhone());
			order.setCountry(orderRequest.getCountry());
			order.setCity(orderRequest.getCity());
			order.setWard(orderRequest.getWards());
			order.setShippingId(shippingRepository.findById(orderRequest.getShippingId())
					.orElseThrow(() -> new RuntimeException("Shipping Method Not Found")));
			order.setPaymentId(paymentRepository.findById(orderRequest.getPaymentId())
					.orElseThrow(() -> new RuntimeException("Payment Method Not Found")));
			order.setCreatedAt(Timestamp.from(Instant.now()));

			// 5. Lưu đơn hàng vào DB
			OrderEntity savedOrder = orderRepository.save(order);

			// 6. Lưu chi tiết sản phẩm
			for (ProductOrderRequest productRequest : orderRequest.getProducts()) {
				ProductSKUEntity productSKU = productSKURepository.findById(productRequest.getProductId())
						.orElseThrow(() -> new RuntimeException("Product Not Found"));

				OrderDetailEntity orderDetail = new OrderDetailEntity();
				orderDetail.setOrderId (savedOrder);
				orderDetail.setProductId(productSKU);
				orderDetail.setQuantity(productRequest.getQuantity());
				orderDetail.setPrice(productSKU.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity())));
				orderDetailRepository.save(orderDetail);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}


	// Hàm ánh xạ ProductEntity sang ProductResponse
	private ProductResponse mapToProductResponse (ProductEntity productEntity, String language, Long productSkuId) {
		ProductResponse productResponse = new ProductResponse ();

		ProductDTO productDTO = new ProductDTO ();
		productDTO.setId (productEntity.getId ());
		productDTO.setPublished (productEntity.isPublished ());
		productDTO.setHotProduct (productEntity.isHotProduct ());
		productDTO.setNewProduct (productEntity.isNewProduct ());
		productDTO.setImage (productEntity.getImage ());
		productDTO.setVideo (productEntity.getVideo ());
		productDTO.setUpdateAt (productEntity.getUpdateAt ());
		productDTO.setCreateAt (productEntity.getCreateAt ());
		productDTO.setDeletedAt (productEntity.getDeletedAt ());

		productResponse.setProduct (productDTO);

		// Ánh xạ thông tin bản dịch (translation) theo ngôn ngữ người dùng
		List<ProductTranslationEntity> translations = productTranslantionRepository.findByProductId (productEntity);
		System.out.println ("Số lượng bản dịch tìm thấy: " + translations.size ());

// Lọc ra bản dịch phù hợp với ngôn ngữ
		Optional<ProductTranslationEntity> translationOpt = translations.stream ()
				.filter (productTranslationEntity -> productTranslationEntity.getLanguage ().equals (language))
				.findFirst (); // Tìm bản dịch đầu tiên phù hợp

// Nếu có bản dịch, ánh xạ nó sang DTO
		translationOpt.ifPresent (productTranslationEntity -> {
			ProductTranslationDTO translationDTO = new ProductTranslationDTO ();
			translationDTO.setId (productTranslationEntity.getId ());
			translationDTO.setLanguage (productTranslationEntity.getLanguage ());
			translationDTO.setName (productTranslationEntity.getName ());
			translationDTO.setDescription (productTranslationEntity.getDescription ());
			translationDTO.setCreatedAt (productTranslationEntity.getCreatedAt ());
			translationDTO.setUpdatedAt (productTranslationEntity.getUpdatedAt ());
			productResponse.setTranslation (translationDTO);
		});
		// Ánh xạ danh sách SKU và variant liên quan
		List<ProductSkuResponse> skuResponses = new ArrayList<> ();
		List<ProductSKUEntity> skuEntities = productSKURepository.findSKUsByProductId (productEntity.getId ());
		for (ProductSKUEntity skuEntity : skuEntities) {
			if (skuEntity.getId () == productSkuId) {
				ProductSkuResponse skuResponse = new ProductSkuResponse ();

				// Ánh xạ thông tin SKU
				ProductSKUDTO skuDTO = new ProductSKUDTO ();
				skuDTO.setId (skuEntity.getId ());
				skuDTO.setImage (skuEntity.getImage ());
				skuDTO.setPrice (skuEntity.getPrice ());
				skuDTO.setQuantity (skuEntity.getQuantity ());
				skuDTO.setCreatedAt (skuEntity.getCreatedAt ());

				skuResponse.setProductSKU (skuDTO);

				// Ánh xạ danh sách variant liên quan
				Map<String, String> attributes = new HashMap<> ();
				List<ProductVariantEntity> variantEntities = productVariantRepository.findVariantsBySKUId (skuEntity.getId ());
				for (ProductVariantEntity variantEntity : variantEntities) {
					attributes.put (variantEntity.getVariantKey (), variantEntity.getValue ()); // Ví dụ: màu sắc
				}

				skuResponse.setVariants (attributes);
				skuResponses.add (skuResponse);
			}

		}

		productResponse.setSkus (skuResponses);
		return productResponse;
	}
}
