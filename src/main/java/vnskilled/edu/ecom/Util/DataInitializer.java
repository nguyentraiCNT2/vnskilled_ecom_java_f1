//package vnskilled.edu.ecom.Util;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//import org.springframework.web.client.RestTemplate;
//import vnskilled.edu.ecom.Entity.Address.AddressCityEntity;
//import vnskilled.edu.ecom.Entity.Address.AddressCountryEntity;
//import vnskilled.edu.ecom.Entity.Address.AddressWardsEntity;
//import vnskilled.edu.ecom.Entity.Categories.CategoriesDescriptionsEntity;
//import vnskilled.edu.ecom.Entity.Categories.CategoriesEntity;
//import vnskilled.edu.ecom.Entity.Otp.OtpTemplateEntity;
//import vnskilled.edu.ecom.Entity.Payment.PaymentEntity;
//import vnskilled.edu.ecom.Entity.Products.ProductEntity;
//import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;
//import vnskilled.edu.ecom.Entity.Products.ProductTranslationEntity;
//import vnskilled.edu.ecom.Entity.Products.ProductVariantEntity;
//import vnskilled.edu.ecom.Entity.Role.RoleEntity;
//import vnskilled.edu.ecom.Entity.Shipping.ShippingEntity;
//import vnskilled.edu.ecom.Entity.User.UserEntity;
//import vnskilled.edu.ecom.Repository.Address.AddressCityRepository;
//import vnskilled.edu.ecom.Repository.Address.AddressCountryRepository;
//import vnskilled.edu.ecom.Repository.Address.AddressWardsRepository;
//import vnskilled.edu.ecom.Repository.Categories.CategoriesDescriptionRepository;
//import vnskilled.edu.ecom.Repository.Categories.CategoryRepository;
//import vnskilled.edu.ecom.Repository.Otp.OtpTemplateRepository;
//import vnskilled.edu.ecom.Repository.Payment.PaymentRepository;
//import vnskilled.edu.ecom.Repository.Product.ProductRepository;
//import vnskilled.edu.ecom.Repository.Product.ProductSKURepository;
//import vnskilled.edu.ecom.Repository.Product.ProductTranslantionRepository;
//import vnskilled.edu.ecom.Repository.Product.ProductVariantRepository;
//import vnskilled.edu.ecom.Repository.Role.RoleRepository;
//import vnskilled.edu.ecom.Repository.Shipping.ShippingRepository;
//import vnskilled.edu.ecom.Repository.User.UserRepository;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//@Configuration
//public class DataInitializer {
//
//	private RoleRepository roleRepository;
//
//	private UserRepository userRepository;
//
//	private AddressCityRepository addressCityRepository;
//
//	private AddressWardsRepository addressWardsRepository;
//
//	private PaymentRepository paymentRepository;
//
//	private ShippingRepository shippingRepository;
//
//	private OtpTemplateRepository otpTemplateRepository;
//
//	private final RestTemplate restTemplate = new RestTemplate();
//    private final ProductRepository productRepository;
//    private final ProductSKURepository productSKURepository;
//    private final ProductVariantRepository productVariantRepository;
//    private final ProductTranslantionRepository productTranslantionRepository;
//    private final CategoryRepository categoriesRepository;
//    private final CategoriesDescriptionRepository categoriesDescriptionsRepository;
//
//    @Autowired
//    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, AddressCityRepository addressCityRepository, AddressWardsRepository addressWardsRepository, PaymentRepository paymentRepository, ShippingRepository shippingRepository, OtpTemplateRepository otpTemplateRepository, ProductRepository productRepository, ProductSKURepository productSKURepository, ProductVariantRepository productVariantRepository, ProductTranslantionRepository productTranslantionRepository, CategoryRepository categoriesRepository, CategoriesDescriptionRepository categoriesDescriptionsRepository, AddressCountryRepository addressCountryRepository) {
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//        this.addressCityRepository = addressCityRepository;
//        this.addressWardsRepository = addressWardsRepository;
//        this.paymentRepository = paymentRepository;
//        this.shippingRepository = shippingRepository;
//        this.otpTemplateRepository = otpTemplateRepository;
//        this.productRepository = productRepository;
//        this.productSKURepository = productSKURepository;
//        this.productVariantRepository = productVariantRepository;
//        this.productTranslantionRepository = productTranslantionRepository;
//        this.categoriesRepository = categoriesRepository;
//        this.categoriesDescriptionsRepository = categoriesDescriptionsRepository;
//        this.addressCountryRepository = addressCountryRepository;
//    }
//
//    // API để lấy dữ liệu cho thành phố, quận huyện và xã phường tại Việt Nam
//	private final String API_URL_PROVINCES = "https://provinces.open-api.vn/api/p";
//	private final String API_URL_DISTRICTS = "https://provinces.open-api.vn/api/d/";
//	private final String API_URL_WARDS = "https://provinces.open-api.vn/api/w/";
//	@Autowired
//	private AddressCountryRepository addressCountryRepository;
//
//	@PostConstruct
//	public void init() {
//		initRoles();
//		initUsers();
//		initPayments ();
//		initShippings ();
//		initOtpTemplates();
//		fetchAndSaveVietnamData(); // Gọi hàm để lấy dữ liệu Việt Nam
//        generateFakeCategoriesData();
//        generateFakeProductData();
//	}
//
//	private void initRoles() {
//		if (roleRepository.count() == 0) {
//			RoleEntity adminRole = new RoleEntity();
//			adminRole.setName("ADMIN");
//			roleRepository.save(adminRole);
//
//			RoleEntity userRole = new RoleEntity();
//			userRole.setName("USER");
//			roleRepository.save(userRole);
//		}
//	}
//
//    private void initShippings() {
//        if (shippingRepository.count() == 0) {
//            ShippingEntity shipping1 = new ShippingEntity();
//            shipping1.setName("Giao hàng qua bưu điện");
//            shipping1.setDescription("Giao hàng nhanh chóng và an toàn");
//            shipping1.setPrice(BigDecimal.valueOf(560000));
//            shipping1.setCreatedAt(Timestamp.from(Instant.now()));
//            shipping1.setUpdatedAt(null);
//
//            ShippingEntity shipping2 = new ShippingEntity();
//            shipping2.setName("Giao hàng bằng xe máy");
//            shipping2.setDescription("Giao hàng trong nội thành");
//            shipping2.setPrice(BigDecimal.valueOf(300000));
//            shipping2.setCreatedAt(Timestamp.from(Instant.now()));
//            shipping2.setUpdatedAt(null);
//
//
//            // Lưu các đối tượng vào repository
//            shippingRepository.saveAll(List.of(shipping1, shipping2));
//        }
//    }
//
//	private void initPayments() {
//		if (paymentRepository.count() == 0) {
//			// Phương thức thanh toán khi nhận hàng
//			PaymentEntity payment1 = new PaymentEntity();
//			payment1.setName("Thanh toán khi nhận hàng");
//			payment1.setDescription("Khách hàng thanh toán trực tiếp khi nhận hàng.");
//			payment1.setCreatedAt(Timestamp.from(Instant.now()));
//			payment1.setUpdatedAt(Timestamp.from(Instant.now()));
//
//			// Phương thức thanh toán qua tài khoản ngân hàng
//			PaymentEntity payment2 = new PaymentEntity();
//			payment2.setName("Thanh toán qua tài khoản ngân hàng");
//			payment2.setDescription("Khách hàng thanh toán qua chuyển khoản ngân hàng.");
//			payment2.setCreatedAt(Timestamp.from(Instant.now()));
//			payment2.setUpdatedAt(Timestamp.from(Instant.now()));
//
//			// Lưu các phương thức thanh toán vào repository
//			paymentRepository.saveAll(List.of(payment1, payment2));
//		}
//	}
//
//	private void initUsers() {
//		if (userRepository.count() == 0) {
//			RoleEntity userRole = roleRepository.findById(1L).orElse(null);
//			String hashPassword = BCrypt.hashpw("admin", BCrypt.gensalt());
//			Timestamp now = Timestamp.from(Instant.now());
//
//			UserEntity user = new UserEntity();
//			user.setEmail("admin@gmail.com");
//			user.setPassword(hashPassword);
//			user.setActive(true);
//			user.setEmailActive(true);
//			user.setPhone("01012345678");
//			user.setFirstName("Super");
//			user.setLastName("Administrator");
//			user.setCreatedAt(now);
//			user.setLanguage("vi");
//			user.setUpdatedAt(now);
//			user.setRoleId(userRole);
//			userRepository.save(user);
//		}
//	}
//	private void initOtpTemplates() {
//		if (otpTemplateRepository.count() == 0) {
//			// Template for VERIFY_ACCOUNT
//			OtpTemplateEntity verifyAccountTemplate = new OtpTemplateEntity();
//			verifyAccountTemplate.setType("VERIFY_ACCOUNT");
//			verifyAccountTemplate.setSubject("Xác thực tài khoản");
//			verifyAccountTemplate.setBody(
//					"<!DOCTYPE html>\n" +
//                            "<html lang=\"en\">\n" +
//                            "<head>\n" +
//                            "    <meta charset=\"UTF-8\">\n" +
//                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                            "    <title>Xác thực tài khoản</title>\n" +
//                            "    <style>\n" +
//                            "        .card {\n" +
//                            "            width: 40%;\n" +
//                            "            border-radius: 20px;\n" +
//                            "            background: #ffffff;\n" +
//                            "            padding: 5px;\n" +
//                            "            overflow: hidden;\n" +
//                            "            box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 20px 0px;\n" +
//                            "            transition: transform 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);\n" +
//                            "            color: #333333;\n" +
//                            "            margin: 0 auto;\n" +
//                            "            font-family: Arial, sans-serif;\n" +
//                            "            margin-top: 50px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card:hover {\n" +
//                            "            transform: scale(1.05);\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .top-section {\n" +
//                            "            height: 150px;\n" +
//                            "            border-radius: 15px;\n" +
//                            "            background: linear-gradient(45deg, rgb(72, 191, 227) 0%, rgb(135, 222, 243) 100%);\n" +
//                            "            position: relative;\n" +
//                            "            display: flex;\n" +
//                            "            flex-direction: column;\n" +
//                            "            align-items: center;\n" +
//                            "            justify-content: center;\n" +
//                            "            padding-top: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .top-section .border {\n" +
//                            "            border-bottom-right-radius: 20px;\n" +
//                            "            border-top-left-radius: 20px;\n" +
//                            "            height: 30px;\n" +
//                            "            width: 130px;\n" +
//                            "            background: #ffffff;\n" +
//                            "            position: relative;\n" +
//                            "            font-size: 24px;\n" +
//                            "            font-weight: bold;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            padding: 10px 10px;\n" +
//                            "            text-align: center;\n" +
//                            "            color: #333333;\n" +
//                            "            margin-left: 33%;" +
//                            "            margin-top: 30px;" +
//                            "            box-shadow: -10px -10px 0 0 #ffffff;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "            .otp-code {\n" +
//                            "             margin: auto;\n" +
//                            "            font-size: 24px;\n" +
//                            "            width: 30%;\n" +
//                            "            font-weight: bold;\n" +
//                            "            background: #f0f4ff;\n" +
//                            "            color: #2e7dff;\n" +
//                            "            padding: 10px 30px;\n" +
//                            "            text-align: center;\n" +
//                            "            border-radius: 5px;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            margin-top: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section {\n" +
//                            "            margin-top: 15px;\n" +
//                            "            padding: 10px 5px;\n" +
//                            "            text-align: center;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .title {\n" +
//                            "            font-size: 17px;\n" +
//                            "            font-weight: bolder;\n" +
//                            "            color: #2e7dff;\n" +
//                            "            text-align: center;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            margin-bottom: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .description {\n" +
//                            "            color: #555555;\n" +
//                            "            font-size: 14px;\n" +
//                            "            margin-bottom: 10px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .expiry {\n" +
//                            "            font-size: 12px;\n" +
//                            "            color: #888888;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .email-footer {\n" +
//                            "            text-align: center;\n" +
//                            "            font-size: 10px;\n" +
//                            "            color: #aaaaaa;\n" +
//                            "            margin-top: 10px;\n" +
//                            "        }\n" +
//                            "    </style>\n" +
//                            "</head>\n" +
//                            "<body>\n" +
//                            "    <div class=\"card\">\n" +
//                            "        <div class=\"top-section\">\n" +
//                            "            <div class=\"border\">\n" +
//                            "                3legant.\n" +
//                            "            </div>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"bottom-section\">\n" +
//                            "            <span class=\"title\">Xác thực tài khoản</span>\n" +
//                            "            <p class=\"description\">\n" +
//                            "                Chào bạn, <br> Đây là mã OTP của bạn để xác thực tài khoản:\n" +
//                            "            </p>\n" +
//                            "            <div class=\"otp-code\">{{otp_code}}</div>\n" +
//                            "\n" +
//                            "            <p class=\"expiry\">Mã này sẽ hết hạn trong 5 phút. Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"email-footer\">© 2024 Your Company. All rights reserved.</div>\n" +
//                            "    </div>\n" +
//                            "</body>\n" +
//                            "</html>\n"
//			);
//			verifyAccountTemplate.setLanguage("vi");
//			verifyAccountTemplate.setCreatedAt(Timestamp.from(Instant.now()));
//			otpTemplateRepository.save(verifyAccountTemplate);
//
//// Template for RESET_PASSWORD
//			OtpTemplateEntity resetPasswordTemplate = new OtpTemplateEntity();
//			resetPasswordTemplate.setType("RESET_PASSWORD");
//			resetPasswordTemplate.setSubject("Thay đổi mật khẩu");
//			resetPasswordTemplate.setBody(
//					"<!DOCTYPE html>\n" +
//                            "<html lang=\"en\">\n" +
//                            "<head>\n" +
//                            "    <meta charset=\"UTF-8\">\n" +
//                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                            "    <title>Thay đổi mật khẩu</title>\n" +
//                            "    <style>\n" +
//                            "        /* Styles for the OTP card */\n" +
//                            "        .card {\n" +
//                            "            width: 40%;\n" +
//                            "            border-radius: 20px;\n" +
//                            "            background: #ffffff;\n" +
//                            "            padding: 5px;\n" +
//                            "            overflow: hidden;\n" +
//                            "            box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 20px 0px;\n" +
//                            "            transition: transform 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);\n" +
//                            "            color: #333333;\n" +
//                            "            margin: 0 auto;\n" +
//                            "            font-family: Arial, sans-serif;\n" +
//                            "            margin-top: 50px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card:hover {\n" +
//                            "            transform: scale(1.05);\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .top-section {\n" +
//                            "            height: 150px;\n" +
//                            "            border-radius: 15px;\n" +
//                            "            background: linear-gradient(45deg, rgb(227, 224, 72) 0%, rgb(243, 241, 135) 100%);\n" +
//                            "            position: relative;\n" +
//                            "            display: flex;\n" +
//                            "            flex-direction: column;\n" +
//                            "            align-items: center;\n" +
//                            "            justify-content: center;\n" +
//                            "            padding-top: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .top-section .border {\n" +
//                            "            border-bottom-right-radius: 20px;\n" +
//                            "            border-top-left-radius: 20px;\n" +
//                            "            height: 30px;\n" +
//                            "            width: 130px;\n" +
//                            "            background: #ffffff;\n" +
//                            "            position: relative;\n" +
//                            "            font-size: 24px;\n" +
//                            "            font-weight: bold;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            padding: 10px 10px;\n" +
//                            "            text-align: center;\n" +
//                            "            color: #333333;\n" +
//                            "            margin-left: 33%;" +
//                            "            margin-top: 30px;" +
//                            "            box-shadow: -10px -10px 0 0 #ffffff;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "            .otp-code {\n" +
//                            "             margin: auto;\n" +
//                            "            font-size: 24px;\n" +
//                            "            width: 30%;\n" +
//                            "            font-weight: bold;\n" +
//                            "            background: #f0f4ff;\n" +
//                            "            color: #decd32;\n" +
//                            "            padding: 10px 30px;\n" +
//                            "            text-align: center;\n" +
//                            "            border-radius: 5px;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            margin-top: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section {\n" +
//                            "            margin-top: 15px;\n" +
//                            "            padding: 10px 5px;\n" +
//                            "            text-align: center;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .title {\n" +
//                            "            font-size: 17px;\n" +
//                            "            font-weight: bolder;\n" +
//                            "            text-align: center;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            margin-bottom: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .description {\n" +
//                            "            color: #555555;\n" +
//                            "            font-size: 14px;\n" +
//                            "            margin-bottom: 10px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .expiry {\n" +
//                            "            font-size: 12px;\n" +
//                            "            color: #888888;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .email-footer {\n" +
//                            "            text-align: center;\n" +
//                            "            font-size: 10px;\n" +
//                            "            color: #aaaaaa;\n" +
//                            "            margin-top: 10px;\n" +
//                            "        }\n" +
//                            "    </style>\n" +
//                            "</head>\n" +
//                            "<body>\n" +
//                            "    <div class=\"card\">\n" +
//                            "        <div class=\"top-section\">\n" +
//                            "            <div class=\"border\">\n" +
//                            "                3legant.\n" +
//                            "            </div>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"bottom-section\">\n" +
//                            "            <span class=\"title\">Thay đổi mật khẩu</span>\n" +
//                            "            <p class=\"description\">\n" +
//                            "                Chào bạn, <br> Đây là mã OTP của bạn để thay đổi mật khẩu:\n" +
//                            "            </p>\n" +
//                            "            <div class=\"otp-code\">{{otp_code}}</div>\n" +
//                            "\n" +
//                            "            <p class=\"expiry\">Mã này sẽ hết hạn trong 5 phút. Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"email-footer\">© 2024 Your Company. All rights reserved.</div>\n" +
//                            "    </div>\n" +
//                            "</body>\n" +
//                            "</html>\n"
//			);
//			resetPasswordTemplate.setLanguage("vi");
//			resetPasswordTemplate.setCreatedAt(Timestamp.from(Instant.now()));
//			otpTemplateRepository.save(resetPasswordTemplate);
//
//// Template for PAYMENT_CONFIRMATION
//			OtpTemplateEntity paymentTemplate = new OtpTemplateEntity();
//			paymentTemplate.setType("PAYMENT_CONFIRMATION");
//			paymentTemplate.setSubject("Xác nhận thanh toán");
//			paymentTemplate.setBody(
//					"<!DOCTYPE html>\n" +
//                            "<html lang=\"en\">\n" +
//                            "<head>\n" +
//                            "    <meta charset=\"UTF-8\">\n" +
//                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                            "    <title>Xác nhận thanh toán</title>\n" +
//                            "    <style>\n" +
//                            "        /* Styles for the OTP card */\n" +
//                            "        .card {\n" +
//                            "            width: 40%;\n" +
//                            "            border-radius: 20px;\n" +
//                            "            background: #ffffff;\n" +
//                            "            padding: 5px;\n" +
//                            "            overflow: hidden;\n" +
//                            "            box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 20px 0px;\n" +
//                            "            transition: transform 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);\n" +
//                            "            color: #333333;\n" +
//                            "            margin: 0 auto;\n" +
//                            "            font-family: Arial, sans-serif;\n" +
//                            "            margin-top: 50px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card:hover {\n" +
//                            "            transform: scale(1.05);\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .top-section {\n" +
//                            "            height: 150px;\n" +
//                            "            border-radius: 15px;\n" +
//                            "            background: linear-gradient(45deg, rgb(72, 227, 85) 0%, rgb(135, 243, 169) 100%);\n" +
//                            "            position: relative;\n" +
//                            "            display: flex;\n" +
//                            "            flex-direction: column;\n" +
//                            "            align-items: center;\n" +
//                            "            justify-content: center;\n" +
//                            "            padding-top: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .top-section .border {\n" +
//                            "            border-bottom-right-radius: 20px;\n" +
//                            "            border-top-left-radius: 20px;\n" +
//                            "            height: 30px;\n" +
//                            "            width: 130px;\n" +
//                            "            background: #ffffff;\n" +
//                            "            position: relative;\n" +
//                            "            font-size: 24px;\n" +
//                            "            font-weight: bold;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            padding: 10px 10px;\n" +
//                            "            text-align: center;\n" +
//                            "            color: #333333;\n" +
//                            "            margin-left: 33%;" +
//                            "            margin-top: 30px;" +
//                            "            box-shadow: -10px -10px 0 0 #ffffff;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "            .otp-code {\n" +
//                            "             margin: auto;\n" +
//                            "            font-size: 24px;\n" +
//                            "            width: 30%;\n" +
//                            "            font-weight: bold;\n" +
//                            "            background: #f0f4ff;\n" +
//                            "            color: #35de32;\n" +
//                            "            padding: 10px 30px;\n" +
//                            "            text-align: center;\n" +
//                            "            border-radius: 5px;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            margin-top: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section {\n" +
//                            "            margin-top: 15px;\n" +
//                            "            padding: 10px 5px;\n" +
//                            "            text-align: center;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .title {\n" +
//                            "            font-size: 17px;\n" +
//                            "            font-weight: bolder;\n" +
//                            "            text-align: center;\n" +
//                            "            letter-spacing: 2px;\n" +
//                            "            margin-bottom: 15px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .description {\n" +
//                            "            color: #555555;\n" +
//                            "            font-size: 14px;\n" +
//                            "            margin-bottom: 10px;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .card .bottom-section .expiry {\n" +
//                            "            font-size: 12px;\n" +
//                            "            color: #888888;\n" +
//                            "        }\n" +
//                            "\n" +
//                            "        .email-footer {\n" +
//                            "            text-align: center;\n" +
//                            "            font-size: 10px;\n" +
//                            "            color: #aaaaaa;\n" +
//                            "            margin-top: 10px;\n" +
//                            "        }\n" +
//                            "    </style>\n" +
//                            "</head>\n" +
//                            "<body>\n" +
//                            "    <div class=\"card\">\n" +
//                            "        <div class=\"top-section\">\n" +
//                            "            <div class=\"border\">\n" +
//                            "                3legant.\n" +
//                            "            </div>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"bottom-section\">\n" +
//                            "            <span class=\"title\">Xác nhận thanh toán</span>\n" +
//                            "            <p class=\"description\">\n" +
//                            "                Chào bạn, <br>  Giao dịch của bạn đã được thực hiện thành công. Mã giao dịch của bạn là:\n" +
//                            "            </p>\n" +
//                            "            <div class=\"otp-code\">{{otp_code}}</div>\n" +
//                            "\n" +
//                            "            <p class=\"expiry\">Mã này sẽ hết hạn trong 5 phút. Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>\n" +
//                            "        </div>\n" +
//                            "        <div class=\"email-footer\">© 2024 Your Company. All rights reserved.</div>\n" +
//                            "    </div>\n" +
//                            "</body>\n" +
//                            "</html>\n"
//			);
//			paymentTemplate.setLanguage("vi");
//			paymentTemplate.setCreatedAt(Timestamp.from(Instant.now()));
//			otpTemplateRepository.save(paymentTemplate);
//
//		}
//	}
//
//	private void fetchAndSaveVietnamData() {
//
//		// Lấy dữ liệu cho tỉnh
//		List<Map<String, Object>> provinces = restTemplate.getForObject(API_URL_PROVINCES, List.class);
//		if (provinces != null) {
//			if(addressCityRepository.count () == 0){
//				AddressCountryEntity addressCountryEntity = new AddressCountryEntity ();
//				addressCountryEntity.setName ("Việt Nam");
//				AddressCountryEntity country =addressCountryRepository.save (addressCountryEntity);
//				for (Map<String, Object> province : provinces) {
//					AddressCityEntity newCity = new AddressCityEntity ();
//					String provinceName = (String) province.get("name"); // Lấy tên tỉnh/thành phố
//					if (!addressCityRepository.existsByName (provinceName)) {
//						newCity.setName(provinceName);
//						newCity.setCountryId (country);
//						addressCityRepository.save(newCity);
//					}
//
//					Integer provinceId = (Integer) province.get("code"); // Lấy ID của tỉnh
//					if (provinceId != null) {
//						// Lấy danh sách quận/huyện theo tỉnh
//						List<Map<String, Object>> districts = restTemplate.getForObject(API_URL_DISTRICTS, List.class);
//						if (districts != null) {
//							// Lọc quận/huyện theo provinceId
//							districts.stream()
//									.filter(district -> !addressWardsRepository.existsByName((String) district.get("name"))) // Kiểm tra tên quận chưa tồn tại
//									.filter(district -> provinceId.equals(district.get("province_code"))) // Sử dụng equals để so sánh
//									.forEach(district -> {
//										String districtName = (String) district.get("name");
//										AddressWardsEntity newDistrict = new AddressWardsEntity();
//										newDistrict.setName(districtName);
//										newDistrict.setCityId(newCity); // Gán thành phố cho quận/huyện
//										addressWardsRepository.save(newDistrict);
//
//										Integer districtId = (Integer) district.get("code"); // Lấy ID của quận
//										// Thêm logic xử lý nếu cần
//									});
//						} else {
//							System.out.println("Không lấy được dữ liệu quận/huyện cho tỉnh: " + provinceName);
//						}
//					} else {
//						System.out.println("Province ID is null for province: " + provinceName);
//					}
//				}
//			}
//
//		} else {
//			System.out.println("Không lấy được dữ liệu Việt Nam từ API");
//		}
//	}
//
//    // Generate fake product data
//    public void generateFakeProductData() {
//        if (productRepository.count() == 0) {
//            List<CategoriesEntity> categories = categoriesRepository.findAll(); // Lấy tất cả danh mục
//            for (int i = 1; i <= 100; i++) {
//                ProductEntity product = new ProductEntity();
//                product.setImage(generateFakeImageJson(i));
//                product.setHotProduct(i % 2 == 0);
//                product.setNewProduct(i % 3 == 0);
//                product.setPublished(true);
//                product.setRating(4.5); // Giả sử sản phẩm có rating
//
//                // Gán danh mục ngẫu nhiên cho sản phẩm
//                if (!categories.isEmpty()) {
//                    CategoriesEntity randomCategory = categories.get(new Random().nextInt(categories.size()));
//                    product.setCategoryId(randomCategory); // Gán danh mục vào sản phẩm
//                }
//
//                ProductEntity savedProduct = productRepository.save(product);
//
//                // ProductTranslationEntity
//                ProductTranslationEntity translation = new ProductTranslationEntity();
//                translation.setLanguage("vi");
//                translation.setName("Sản phẩm " + i);
//                translation.setDescription("Mô tả sản phẩm " + i);
//                translation.setProductId(savedProduct);
//                productTranslantionRepository.save(translation);
//
//                // Generate SKUs
//                for (int j = 1; j <= 3; j++) {
//                    ProductSKUEntity sku = new ProductSKUEntity();
//                    sku.setImage("https://www.google.com/imgres?q=tai%20nghe%20bluetooth&imgurl=http%3A%2F%2Fcdn.dienmaygiakhanh.com%2FProducts%2FImages%2F54%2F243041%2Fbluetooth-true-wireless-jbl-t115twswhtas-thumb-1-600x600.jpeg&imgrefurl=https%3A%2F%2Fdienmaygiakhanh.com%2Fphu-kien%2Ftai-nghe-bluetooth-true-wireless-jbl-t115tws-p17354.html&docid=xguGn6OLZXyHYM&tbnid=1VxLbA3kX_LNJM&vet=12ahUKEwjI7_myzKiJAxU3rlYBHYDbHRsQM3oECFcQAA..i&w=600&h=600&hcb=2&ved=2ahUKEwjI7_myzKiJAxU3rlYBHYDbHRsQM3oECFcQAA");
//                    sku.setPrice(BigDecimal.valueOf(100 + (j * 10)));
//                    sku.setQuantity(50L);
//                    sku.setProductId(savedProduct);
//                    ProductSKUEntity savedSKU = productSKURepository.save(sku);
//
//                    // Generate Variants
//                    for (int k = 1; k <= 2; k++) {
//                        ProductVariantEntity variant = new ProductVariantEntity();
//                        variant.setVariantKey("variantKey" + k);
//                        variant.setValue("Giá trị " + k);
//                        variant.setVariantId(savedSKU);
//                        productVariantRepository.save(variant);
//                    }
//                }
//            }
//        }
//    }
//
//    // Generate fake categories data with a maximum of 3 levels
//    public void generateFakeCategoriesData() {
//        if (categoriesRepository.count() == 0){
//            List<CategoriesEntity> parentCategories = new ArrayList<>();
//
//            for (int i = 1; i <= 5; i++) {
//                CategoriesEntity category = new CategoriesEntity();
//                category.setImage("category_image" + i + ".jpg");
//                CategoriesEntity savedCategory = categoriesRepository.save(category);
//
//                // Create a category description
//                createCategoryDescription(savedCategory, "Danh mục cha " + i, "Mô tả danh mục cha " + i);
//
//                // Generate subcategories up to 2 levels
//                for (int j = 1; j <= 3; j++) {
//                    CategoriesEntity subCategory = new CategoriesEntity();
//                    subCategory.setImage("subcategory_image" + i + "_" + j + ".jpg");
//                    subCategory.setParentId(savedCategory.getId());
//                    CategoriesEntity savedSubCategory = categoriesRepository.save(subCategory);
//
//                    // Create a subcategory description
//                    createCategoryDescription(savedSubCategory, "Danh mục con " + i + "." + j, "Mô tả danh mục con " + i + "." + j);
//
//                    parentCategories.add(savedSubCategory);
//                }
//            }
//        }
//
//    }
//
//    private void createCategoryDescription(CategoriesEntity category, String name, String description) {
//        CategoriesDescriptionsEntity desc = new CategoriesDescriptionsEntity();
//        desc.setCategoryId(category);
//        desc.setName(name);
//        desc.setDescription(description);
//        desc.setLanguage("vi");
//        categoriesDescriptionsRepository.save(desc);
//    }
//
//    // Hàm tạo chuỗi JSON chứa các liên kết hình ảnh
//    private String generateFakeImageJson(int productId) {
//        return String.format("[\"https://www.thegioididong.com/tai-nghe-bluetooth\", " +
//                        "\"https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/g/r/group_169_2.png\", " +
//                        "\"https://cdn.tgdd.vn/Files/2023/03/29/1522094/30-010423-223344.jpg\"]",
//                productId, productId, productId);
//    }
//}
