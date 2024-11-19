package vnskilled.edu.ecom.Configuration.Security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Service.RedisService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisService redisService;

    @Autowired
    public JwtRequestFilter(@Lazy CustomUserDetailsService jwtUserDetailsService,
                            JwtTokenUtil jwtTokenUtil,
                            RedisService redisService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        createRequestContext(request, response);

        try {
            String jwtToken = extractJwtToken(request);
            if (jwtToken != null) {
                processAuthentication(request, jwtToken);
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            handleAuthenticationException(response, e);
        } finally {
            logRequestDetails(request, response);
            RequestContext.clear();
        }
    }



    private void createRequestContext(HttpServletRequest request, HttpServletResponse responseWrapper) {
        RequestContext requestContext = new RequestContext();
        requestContext.setRequestId(UUID.randomUUID().toString());
        requestContext.setTimestamp(Timestamp.from (Instant.now ()));
        requestContext.setRequestURL(request.getRequestURL().toString());
        requestContext.setMethod(request.getMethod());
        requestContext.setResponseStatus(responseWrapper.getStatus());
        requestContext.setIpAddress(getClientIpAddress(request));
        requestContext.setHostName(getHostName());
        requestContext.setUserAgent(request.getHeader("User-Agent"));
        RequestContext.set(requestContext);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        return (ipAddress != null && !ipAddress.isEmpty()) ? ipAddress : request.getRemoteAddr();
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    private void processAuthentication(HttpServletRequest request, String jwtToken) {
        validateToken(jwtToken);
        String username = extractUsername(jwtToken);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            authenticateUser(request, username, jwtToken);
        }
    }

    private String extractUsername(String jwtToken) {
        try {
            return jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void validateToken(String jwtToken) {
        if (redisService.isTokenExists(jwtToken)) {
            throw new RuntimeException("Phiên đăng nhập đã hết hạn!");
        }
    }

    private void authenticateUser(HttpServletRequest request, String username, String jwtToken) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
			RequestContext requestContext = new RequestContext();
			requestContext.setRole(userDetails.getAuthorities().toString());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            throw new RuntimeException("Phiên đăng nhập của bạn đã kết thúc, vui lòng đăng nhập lại");
        }
    }

    private void handleAuthenticationException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        response.getWriter().flush();
    }

    private void logRequestDetails(HttpServletRequest request, HttpServletResponse responseWrapper) {
        RequestContext requestContext = RequestContext.get();
        if (requestContext != null) {
            requestContext.setResponseStatus(responseWrapper.getStatus());
            requestContext.setRequestURL(request.getRequestURL().toString());
            requestContext.setMethod(request.getMethod());
        }
    }
}
