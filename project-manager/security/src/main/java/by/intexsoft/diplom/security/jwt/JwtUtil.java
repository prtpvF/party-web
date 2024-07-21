package by.intexsoft.diplom.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    private final RedisTemplate<String, String> redisTemplate;

    public String generateToken( String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(10).toInstant());
        String token = JWT.create().withSubject("User details").withClaim("username", username)
                .withIssuedAt(new Date()).withIssuer("free-party").withExpiresAt(expirationDate).sign(Algorithm.HMAC256(secret));
        saveTokenToRedis(username, token);
        return token;
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("free-party")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    public void isTokenActive(String username){
        String token = redisTemplate.opsForValue().get(username);
        if(token.equals(null)){
            throw new TokenExpiredException("token has expired", null);
        }
         log.info("token is still active");
    }

    public void removeToken(String username){
        redisTemplate.delete(username);
        log.info("token has successfully deleted from redis");
    }

    public String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private void saveTokenToRedis(String username, String token){
        redisTemplate.opsForValue().set(username, token, Duration.ofMinutes(10));
        log.info("token has successfully added into redis");
    }
}