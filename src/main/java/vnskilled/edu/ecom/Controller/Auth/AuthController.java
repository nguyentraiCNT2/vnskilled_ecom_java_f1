package vnskilled.edu.ecom.Controller.Auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequestMapping(AuthApiPaths.BASE_PATH)
public class AuthController {
	private final PasswordEncoder passwordEncoder;
	private UserService userService;
    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private RedisService redisService;
    @Autowired
    public AuthController(UserService userService, JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService, RedisService redisService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService = redisService;
	    this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(AuthApiPaths.LOGIN)
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
            if (!passwordEncoder.matches (userRequest.getPassword(), userDetails.getPassword())) {
                return buildErrorResponse("Mật khẩu không chính xác", HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> response = generateTokensWithContext(userDetails);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(AuthApiPaths.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterReuest dto) {
        try {
            userService.register(dto);
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

    @PostMapping(AuthApiPaths.LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return buildErrorResponse("Logout failed: bạn chưa đăng nhập!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String jwtToken = extractJwtToken(request);
            if (jwtToken != null) {
                redisService.save(authentication.getName(), jwtToken);
            }

            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    @PostMapping(AuthApiPaths.FORGOT_PASSWORD)
    public ResponseEntity<?> generateOtpResetPassword(@RequestParam("email") String email) {
        try {
            userService.generateOtpResetPassword(email);
            return ResponseEntity.ok("OTP has been sent to your email.");
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(AuthApiPaths.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok("Password has been reset.");
        } catch (Exception e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
