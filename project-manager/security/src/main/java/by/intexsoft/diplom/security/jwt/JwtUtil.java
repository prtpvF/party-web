package by.intexsoft.diplom.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.lifetime}")
        private long jwtExpirationInMs;

        @Value("${refresh-authorization.lifetime}")
        private long refreshExpirationInMs;


        private final RedisTemplate<String, String> redisTemplate;

        public String generateToken(String username) {
            long expiration = jwtExpirationInMs;
            Date currentDate = new Date();
            long now = currentDate.getTime();
            String authorization = JWT.create().withSubject("User details")
                    .withClaim("username", username)
                    .withIssuedAt(new Date()).withIssuer("free-party")
                    .withExpiresAt(Instant.ofEpochSecond(now + expiration))
                    .sign(Algorithm.HMAC256(secret));
            saveTypeTokenToRedis("access", username, authorization);
            return authorization;
        }

        public String generateRefreshToken(String username) {
            long expiration = refreshExpirationInMs;
            long now = getConvertedCurrentDateAsLong();
            String authorization = JWT.create().withSubject("User details")
                    .withClaim("username", username)
                    .withIssuedAt(new Date()).withIssuer("free-party")
                    .withExpiresAt(Instant.ofEpochSecond(now + expiration))
                    .sign(Algorithm.HMAC256(secret));
            saveTypeTokenToRedis("refresh", username, authorization);
            return authorization;
        }

        public String validateTokenAndRetrieveClaim(String authorization) throws JWTVerificationException {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withSubject("User details")
                    .withIssuer("free-party")
                    .build();
            DecodedJWT jwt = verifier.verify(authorization);
            return jwt.getClaim("username").asString();
        }

        public void isAccessTokenActive(String username) {
            String authorization = redisTemplate.opsForValue()
                    .get(username + ":" + "access");
            assert authorization != null;
            log.info("access token is still active");
        }

        public void isRefreshTokenActive(String username) {
            String authorization = redisTemplate.opsForValue()
                    .get(username + ":" + "refresh");
            assert authorization != null;
            log.info("refresh token is still active");
        }

        public void removeTokens(String username) {
            redisTemplate.delete(username + ":" + "refresh");
            redisTemplate.delete(username + ":" + "access");
            log.info("access token has successfully deleted from redis");
        }

        public String extractTokenFromHeader(HttpServletRequest request) {
            String authorization = request.getHeader("authorization");
            return authorization;
        }

        /**
         * Method adds type of key for storaging in redis db.
         * If type==access than a token will be deleted after a day
         * else a token will be deleted after 7 days
         * @param type type of token what will be saved in db
         * @param username person's username what will be added as a key
         * @param authorization encrypted person's data
         */
        private void saveTypeTokenToRedis(String type, String username,
                                          String authorization) {
            if (type.equals("access")) {
                redisTemplate.opsForValue()
                        .set(username + ":" + type, authorization, jwtExpirationInMs);
            } else {
                redisTemplate.opsForValue()
                        .set(username + ":" + type, authorization, jwtExpirationInMs);
            }
            log.info("token has successfully added into redis");
        }

        private long getConvertedCurrentDateAsLong() {
            Date currentDate = new Date();
            return currentDate.getTime();
        }
}