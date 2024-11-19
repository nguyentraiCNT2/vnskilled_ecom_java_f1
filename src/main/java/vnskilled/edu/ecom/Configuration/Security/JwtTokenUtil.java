package vnskilled.edu.ecom.Configuration.Security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;
    @Value("${jwt.key}")
    private String SECRET_KEY;
    private static final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails, Long userid) {
        Map<String, Object> claims = new HashMap<>();
		claims.put ("email", userDetails.getUsername());
		claims.put ("userId", userid);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256  , SECRET_KEY)
                .compact();
    }

    public boolean invalidateToken(String token) {
        try {
            // Giải mã token để lấy claims
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // Kiểm tra xem token có hết hạn hay không
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date());
        } catch (SignatureException e) {
            // Token không hợp lệ
            return false;
        } catch (ExpiredJwtException e) {
            // Token đã hết hạn
            return false;
        } catch (MalformedJwtException e) {
            // Token không đúng định dạng
            return false;
        } catch (Exception e) {
            // Xử lý lỗi khác (nếu cần)
            return false;
        }
    }
    public String generateRefreshToken(UserDetails userDetails) {
        // Tạo refresh token với thời gian tồn tại dài hơn
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 259200000L))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            // Giải mã và xác thực refresh token
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException e) {
            return false;
        }
    }

    public String getUsernameFromRefreshToken(String refreshToken) {
        // Lấy username từ refresh token
        Claims claims =   Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        return claims.getSubject();
    }
    public Boolean isTokenInvalidated(String token) {
        return blacklistedTokens.contains(token);
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
