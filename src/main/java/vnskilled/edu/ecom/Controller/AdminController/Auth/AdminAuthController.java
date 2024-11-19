package vnskilled.edu.ecom.Controller.AdminController.Auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vnskilled.edu.ecom.Configuration.Security.CustomUserDetailsService;
import vnskilled.edu.ecom.Configuration.Security.JwtTokenUtil;
import vnskilled.edu.ecom.Model.Request.Auth.RegisterReuest;
import vnskilled.edu.ecom.Model.Request.Auth.ResetPasswordRequest;
import vnskilled.edu.ecom.Model.Request.Otp.OtpRequest;
import vnskilled.edu.ecom.Util.EndpointConstant.AuthApiPaths;
import vnskilled.edu.ecom.Model.DTO.User.EmailResetDTO;
import vnskilled.edu.ecom.Model.DTO.Otp.OtpDTO;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Model.Request.User.UserRequest;
import vnskilled.edu.ecom.Service.RedisService;
import vnskilled.edu.ecom.Service.User.UserService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {
	private UserService userService;
	private CustomUserDetailsService customUserDetailsService;
	private JwtTokenUtil jwtTokenUtil;
	private RedisService redisService;
	@Autowired
	public AdminAuthController (UserService userService, CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil, RedisService redisService) {
		this.userService = userService;
		this.customUserDetailsService = customUserDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.redisService = redisService;
	}

	@GetMapping("/login")
	public String login() {
		return "Auth/login";
	}
	@PostMapping("/login")
	public String login(@ModelAttribute UserRequest userRequest, Model model) {
		try {
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
			if (!BCrypt.checkpw(userRequest.getPassword(), userDetails.getPassword())) {
				model.addAttribute("error", "Mật khẩu không chính xác");
				return "Auth/login";
			}
			if (!userDetails.getAuthorities().stream()
					.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
				model.addAttribute("error", "Bạn không đủ quyền hạn để thực hiện đăng nhập");
				return "Auth/login";
			}

			// Tạo token hoặc thực hiện các logic đăng nhập ở đây
			Map<String, Object> response = generateTokensWithContext(userDetails);

			// Chuyển hướng sang trang admin/home nếu đăng nhập thành công
			return "redirect:/admin/home";
		} catch (Exception e) {
			model.addAttribute("error", "Đăng nhập thất bại: " + e.getMessage());
			return "Auth/login";
		}
	}

	@PostMapping(AuthApiPaths.REGISTER)
	public ResponseEntity<?> register(@RequestBody RegisterReuest dto) {
		try {
			UserDTO userDTO = userService.register(dto);
			userDTO.setPassword(null);
			return ResponseEntity.ok("Bạn đã đăng ký thành công, hãy kiểm tra email để xác thực tài khoản.");
		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(AuthApiPaths.RESEND_EMAIL)
	public ResponseEntity<?> reSendEmail(@RequestBody String email) {
		try {
			UserDTO userDTO = userService.reSendEmail(email);
			userDTO.setPassword(null);

			Map<String, Object> response = generateResponseWithContext(userDTO);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(AuthApiPaths.ACTIVATE_EMAIL)
	public ResponseEntity<?> activeEmail(@RequestBody OtpRequest  request) {
		try {
			OtpDTO otpDTO = new OtpDTO();
			otpDTO.setOtpCode(request.getOtp ());

			EmailResetDTO emailResetDTO = new EmailResetDTO();
			emailResetDTO.setEmail(request.getEmail ());
			emailResetDTO.setOtpId(otpDTO);

			userService.activeEmail(emailResetDTO);

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail ());
			Map<String, Object> response = generateTokensWithContext(userDetails);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(AuthApiPaths.REFRESH_TOKEN)
	public ResponseEntity<?> refresh(@RequestParam("refreshToken") String refreshToken) {
		try {
			if (!jwtTokenUtil.validateRefreshToken(refreshToken)) {
				return buildErrorResponse("Refresh token không hợp lệ", HttpStatus.UNAUTHORIZED);
			}

			String username = jwtTokenUtil.getUsernameFromRefreshToken(refreshToken);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

			Map<String, Object> response = generateAccessTokenResponse(userDetails);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			// Lấy thông tin xác thực từ SecurityContext
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				// Invalidate the session
				HttpSession session = request.getSession(false);
				if (session != null) {
					session.invalidate(); // Hủy phiên làm việc
				}

				// Xóa thông tin xác thực
				SecurityContextHolder.clearContext();
			}

			redirectAttributes.addFlashAttribute("message", "Logout successful");
			return "redirect:/login"; // Chuyển hướng đến trang đăng nhập
		} catch (Exception e) {
			return "redirect:/login";
		}
	}

	private String extractJwtToken(HttpServletRequest request) {
		final String tokenHeader = request.getHeader("Authorization");
		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			return tokenHeader.substring(7);
		}
		return null;
	}

	private ResponseEntity<?> buildErrorResponse(String message, HttpStatus status) {
		RequestContext  requestContext =RequestContext.get ();
		return  ResponseEntity.status(status).body (Map.of("message",message,
				"status",status.value(),
				"requestUrl",requestContext.getRequestURL (),
				"requestId",requestContext.getRequestId (),
				"timestamp",requestContext.getTimestamp ()));
	}

	private Map<String, Object> generateTokensWithContext(UserDetails userDetails) {
		RequestContext requestContext= RequestContext.get();
		String accessToken = jwtTokenUtil.generateToken(userDetails, requestContext.getUserId());
		String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

		Map<String, Object> response = new HashMap<>();
		response.put("accessToken", accessToken);
		response.put("refreshToken", refreshToken);
		addRequestContextToResponse(response);

		return response;
	}

	private Map<String, Object> generateResponseWithContext(Object userDetail) {
		Map<String, Object> response = new HashMap<>();
		addRequestContextToResponse(response);
		response.put("userDetail", userDetail);
		return response;
	}

	private Map<String, Object> generateAccessTokenResponse(UserDetails userDetails) {
		RequestContext requestContext= RequestContext.get();
		String accessToken = jwtTokenUtil.generateToken(userDetails, requestContext.getUserId());

		Map<String, Object> response = new HashMap<>();
		response.put("accessToken", accessToken);
		response.put("userId", userDetails.getUsername());
		response.put("role", userDetails.getAuthorities());

		return response;
	}

	private void addRequestContextToResponse(Map<String, Object> response) {
		RequestContext context = RequestContext.get();
		if (context != null) {
			response.put("requestId", context.getRequestId());
			response.put("userId", context.getUserId());
			response.put("timestamp", context.getTimestamp());
			response.put("role", context.getRole());
		}
	}

	@GetMapping("/forgot-password")
	public String showForgotPasswordForm() {
		return "forgot-password"; // Trả về trang để người dùng nhập email
	}

	@PostMapping("/forgot-password")
	public String generateOtpResetPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
		try {
			userService.generateOtpResetPassword(email);
			redirectAttributes.addFlashAttribute("message", "OTP đã được gửi đến email của bạn.");
			return "redirect:/auth/validate-otp"; // Chuyển hướng đến trang xác thực OTP
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/auth/forgot-password"; // Quay lại trang quên mật khẩu nếu có lỗi
		}
	}
//
//	@GetMapping("/validate-otp")
//	public String showValidateOtpForm() {
//		return "validate-otp"; // Trả về trang để người dùng nhập OTP
//	}
//
//	@PostMapping("/validate-otp")
//	public String validateOtp(@RequestParam("email") String email, @RequestParam("otp") String otpCode, RedirectAttributes redirectAttributes) {
//		try {
//			boolean isValid = userService.validateOtp(email, otpCode);
//			if (isValid) {
//				redirectAttributes.addFlashAttribute("message", "OTP là hợp lệ!");
//				return "redirect:/auth/reset-password"; // Chuyển hướng đến trang đặt lại mật khẩu
//			} else {
//				redirectAttributes.addFlashAttribute("error", "OTP không hợp lệ!");
//				return "redirect:/auth/validate-otp"; // Quay lại trang xác thực OTP
//			}
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("error", e.getMessage());
//			return "redirect:/auth/validate-otp"; // Quay lại trang xác thực OTP nếu có lỗi
//		}
//	}

//	@GetMapping("/reset-password")
//	public String showResetPasswordForm() {
//		return "reset-password"; // Trả về trang để người dùng nhập mật khẩu mới
//	}
//
//	@PostMapping("/reset-password")
//	public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String newPassword, RedirectAttributes redirectAttributes) {
//		try {
//			userService.resetPassword(email, newPassword);
//			redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được đặt lại thành công.");
//			return "redirect:/auth/login"; // Chuyển hướng đến trang đăng nhập
//		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("error", e.getMessage());
//			return "redirect:/auth/reset-password"; // Quay lại trang đặt lại mật khẩu nếu có lỗi
//		}
//	}
}
