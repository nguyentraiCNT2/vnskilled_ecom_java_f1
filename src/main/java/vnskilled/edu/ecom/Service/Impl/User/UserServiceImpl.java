package vnskilled.edu.ecom.Service.Impl.User;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Carts.ShoppingCartEntity;
import vnskilled.edu.ecom.Entity.Otp.OtpEntity;
import vnskilled.edu.ecom.Entity.Otp.OtpTemplateEntity;
import vnskilled.edu.ecom.Entity.Role.RoleEntity;
import vnskilled.edu.ecom.Entity.User.EmailResetEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.User.EmailResetDTO;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;
import vnskilled.edu.ecom.Model.Request.Auth.RegisterReuest;
import vnskilled.edu.ecom.Model.Request.Auth.ResetPasswordRequest;
import vnskilled.edu.ecom.Repository.Otp.OtpTemplateRepository;
import vnskilled.edu.ecom.Repository.ShoppingCart.ShoppingCartRepository;
import vnskilled.edu.ecom.Repository.User.EmailResetRepository;
import vnskilled.edu.ecom.Repository.Otp.OtpRepository;
import vnskilled.edu.ecom.Repository.Role.RoleRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.User.UserService;
import vnskilled.edu.ecom.Util.PasswordValidator;
import vnskilled.edu.ecom.Util.RandomId;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailResetRepository emailResetRepository;
    private final OtpRepository otpRepository;
    private final ModelMapper modelMapper;
    private final OtpTemplateRepository otpTemplateRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, JavaMailSender mailSender, UserRepository userRepository, RoleRepository roleRepository, EmailResetRepository emailResetRepository, OtpRepository otpRepository, ModelMapper modelMapper, OtpTemplateRepository otpTemplateRepository, ShoppingCartRepository shoppingCartRepository) {
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailResetRepository = emailResetRepository;
        this.otpRepository = otpRepository;
        this.modelMapper = modelMapper;
        this.otpTemplateRepository = otpTemplateRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }


    // đăng ký tài khoản người dùng
    @Override
    public UserDTO register(RegisterReuest registerReuest) {
        PasswordValidator.validatePassword(registerReuest.getPassword());
        if (userRepository.existsByEmail(registerReuest.getEmail())) {
            log.error("Địa chỉ E-mail đã được sử dụng");
            throw new RuntimeException("Địa chỉ E-mail đã được sử dụng");
        }
        RoleEntity userRole = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại."));
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartEntity = shoppingCartRepository.save(shoppingCartEntity);
        UserEntity userEntity = new UserEntity();
        ;
        userEntity.setPassword(passwordEncoder.encode(registerReuest.getPassword()));
        userEntity.setEmail(registerReuest.getEmail());
        userEntity.setFirstName(registerReuest.getFirstName());
        userEntity.setLastName(registerReuest.getLastName());
        userEntity.setActive(true);
        userEntity.setEmailActive(false);
        userEntity.setLanguage("vi");
        userEntity.setRoleId(userRole);
        userEntity.setCart(shoppingCartEntity);
        UserEntity user = userRepository.save(userEntity);
        return sendEmail(user);
    }

    // gửi mã xác thực
    @Async
    @Override
    public UserDTO sendEmail(UserEntity user) {
        String code = RandomId.generateMKC2(6);
        String to = user.getEmail();
        OtpTemplateEntity template = otpTemplateRepository.findByType("VERIFY_ACCOUNT")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy template OTP cho xác thực tài khoản"));
        String subject = template.getSubject();
        String emailBody = template.getBody().replace("{{otp_code}}", code);
        OtpEntity otp = new OtpEntity();
        otp.setOtpCode(code);
        otp.setCreateAt(Timestamp.from(Instant.now()));
        OtpEntity savedOTP = otpRepository.save(otp);
        EmailResetEntity emailReset = new EmailResetEntity();
        emailReset.setEmail(to);
        emailReset.setOtpId(savedOTP);
        emailReset.setCreatedAt(Timestamp.from(Instant.now()));
        emailReset.setUserId(user);
        emailResetRepository.save(emailReset);
        new Thread (() -> sendEmailAsync (to, subject, emailBody, code)).start ();
        return modelMapper.map(user, UserDTO.class);
    }

    // gửi lại mã xác thực
    @Override
    public UserDTO reSendEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity.isEmailActive()) {
            throw new RuntimeException("Email đã được xác thực, không cần gửi lại OTP.");
        }
        Timestamp now = Timestamp.from(Instant.now());
        List<EmailResetEntity> recentRequests = emailResetRepository.findByEmail(email);
        if (recentRequests.size() >= 3) {
            throw new RuntimeException("Bạn đã yêu cầu OTP quá 3 lần trong 1 giờ. Vui lòng thử lại sau.");
        }

        String code = RandomId.generateMKC2(6);
        // Lấy template từ bảng otp_templates
        OtpTemplateEntity template = otpTemplateRepository.findByType("VERIFY_ACCOUNT")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy template OTP cho xác thực tài khoản"));

        String subject = template.getSubject();
        // Thay thế mã OTP vào nội dung body của template
        String emailBody = template.getBody().replace("{{otp_code}}", code);

        OtpEntity otp = recentRequests.get(0).getOtpId();
        otp.setOtpCode(code);
        otp.setUpdateAt(now);
        otpRepository.save(otp);

        recentRequests.get(0).setCreatedAt(now);
        emailResetRepository.save(recentRequests.get(0));

        new Thread (() -> sendEmailAsync (email, subject, emailBody, code)).start ();


        return modelMapper.map(userEntity, UserDTO.class);
    }

  //  tìm kiếm tài khoản và gửi mã
    @Override
    public void generateOtpResetPassword(String email) {
        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new EntityNotFoundException("Email " + email + " not found");
            }
            String code = RandomId.generateMKC2(6);
            Timestamp now = Timestamp.from(Instant.now());
            // Lấy template từ bảng otp_templates
            OtpTemplateEntity template = otpTemplateRepository.findByType("RESET_PASSWORD")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy template OTP cho xác thực tài khoản"));

            String subject = template.getSubject();
            // Thay thế mã OTP vào nội dung body của template
            String emailBody = template.getBody().replace("{{otp_code}}", code);
            //Đã có otp chưa
            OtpEntity otp = new OtpEntity();
            otp.setOtpCode(code);
            otp.setCreateAt(now);
            otp.setUserId(user);
            otp.setUpdateAt(null);
           otpRepository.save(otp);

            new Thread(() -> {
                try {
                    // Gọi phương thức gửi email bất đồng bộ
                    sendEmailAsync(email, subject, emailBody, code);
                } catch (Exception e) {
                    System.err.println("Lỗi gửi email: " + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    // kiểm tra mã xác thự
    private boolean validateOtp(UserEntity user, String otpCode) {
        try {
            List<OtpEntity> otpEntities = otpRepository.findByUserId(user);
            otpEntities.stream()
                    .filter(otpEntity -> "RESET_PASSWORD".equals(otpEntity.getType()))
                    .forEach(otpEntity -> {
                        if (!otpEntity.getCreateAt().after(Timestamp.from(Instant.now()))) {
                            throw new IllegalArgumentException("Mã OTP không hợp lệ");
                        }
                        if (!otpEntity.getOtpCode().equals(otpCode)) {
                            throw new IllegalArgumentException("Mã OTP không hợp lệ");
                        }
                    });
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // thay đổi mật khẩu
    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        try {
            UserEntity user = userRepository.findByEmail(resetPasswordRequest.getEmail());
            if (user == null) {
                throw new EntityNotFoundException("Email " + resetPasswordRequest.getEmail() + " không tồn tại");
            }

            boolean isOtpValid = validateOtp(user, resetPasswordRequest.getOtp());
            if (isOtpValid) {
                PasswordValidator.validatePassword(resetPasswordRequest.getNewPassword());
                user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
                userRepository.save(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    // kích hoạt tài khoản
    @Override
    public void activeEmail(EmailResetDTO emailResetDTO) {
        UserEntity userEntity = userRepository.findByEmail(emailResetDTO.getEmail());
        if (userEntity == null) {
            throw new EntityNotFoundException("Không có tài khoản nào có email này");
        }
        if (userEntity.isEmailActive()) {
            throw new RuntimeException("Tài khoản đã được xác thực, vui lòng đăng nhập");
        }

        List<EmailResetEntity> emailResets = emailResetRepository.findByEmail(emailResetDTO.getEmail());

        Optional<EmailResetEntity> matchingEmailReset = emailResets.stream()
                .filter(emailReset ->
                        emailReset.getOtpId() != null && emailResetDTO.getOtpId() != null &&
                                emailResetDTO.getOtpId().getOtpCode().equals(emailReset.getOtpId().getOtpCode())
                )
                .findFirst();

        if (matchingEmailReset.isPresent()) {
            userEntity.setEmailActive(true);
            userRepository.save(userEntity);
        } else {
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }
    }

    @Async
    public void sendEmailAsync(String to, String subject, String body, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // `true` để gửi email dạng HTML
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // thông tin cá nhân
    @Override
    public UserDTO me() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User Not Found");
            }
            return modelMapper.map(user, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // cập nhật thông tin cá nhân
    @Override
    public void updateUser(UserDTO userDTO) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User Not Found");
            }
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setPhone(userDTO.getPhone());
            user.setUpdatedAt(Timestamp.from(Instant.now()));

            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // thay đổi mật khẩu
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User Not Found");
            }
            if (! passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new RuntimeException("Mat khau khong trung khop");
            }
            if (oldPassword.equals(user.getPassword())) {
                throw new RuntimeException("Mat khau khong the trung voi mat khau cu");

            }
            PasswordValidator.validatePassword(newPassword);
            String passwordHad = passwordEncoder.encode(newPassword);
            user.setPassword(passwordHad);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void activateUser(Long id) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User NOT FOUND"));
            user.setActive(true);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void banUser(Long id) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User NOT FOUND"));
            user.setActive(false);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void updateRole(Long id, Long role) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User NOT FOUND"));
            RoleEntity roleEntity = roleRepository.findById(role).orElseThrow(() -> new RuntimeException("Role NOT FOUND"));
            user.setRoleId(roleEntity);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getUserByRole(Long roleId) {
        try {
        List<UserDTO> result = new ArrayList<>();
        List<UserEntity> userEntities= userRepository.findUserByRoleId(roleId);
        if (userEntities.isEmpty())
            throw new RuntimeException("User Not found");
        for (UserEntity userEntity : userEntities) {
            result.add(modelMapper.map(userEntity, UserDTO.class));
        }
        return result;
    }catch (Exception e){
        throw new RuntimeException(e.getMessage());
    }
    }

    @Override
    public List<UserDTO> filterUser(String email, Long roleId, Boolean active, Boolean emailActive) {
        try {
            List<UserDTO> result = new ArrayList<>();
            List<UserEntity> userEntities= userRepository.findUsersByCriteria(email, roleId, active, emailActive);
          if (userEntities.isEmpty())
              throw new RuntimeException("User Not found");
            for (UserEntity userEntity : userEntities) {
                result.add(modelMapper.map(userEntity, UserDTO.class));
            }
            return result;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public UserDTO getUserById(Long id) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User NOT FOUND"));
            return modelMapper.map(user, UserDTO.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
