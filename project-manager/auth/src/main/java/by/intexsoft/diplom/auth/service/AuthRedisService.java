package by.intexsoft.diplom.auth.service;

import by.intexsoft.diplom.auth.exception.TokenHasExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthRedisService {

        private final RedisTemplate<String, String> redisTemplate;

        private final String ACCESS_KEY_PREFIX = "access_token";
        private final String REFRESH_KEY_PREFIX = "refresh_token";

        public void addAccessTokenIntoRedis(String accessToken, String username) {
            String key = ACCESS_KEY_PREFIX + ":" + username;
            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                redisTemplate.opsForValue().set(key,
                        accessToken,
                        30,
                        TimeUnit.MINUTES);
                log.info("Add access token into redis");
            }
            else {
                log.warn("something get wrong with saving access token into redis");
            }
        }

        public void addRefreshTokenIntoRedis(String refreshToken, String username) {
            String key = REFRESH_KEY_PREFIX + ":" + username;
            if(Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                redisTemplate.opsForValue().set(key,
                        refreshToken,
                        7,
                        TimeUnit.DAYS);
                log.info("Add refresh token into redis");
            }
            else {
                log.warn("something get wrong with saving refresh token into redis");
            }
        }

        public void removeAllTokensFromRedis(String username) {
            redisTemplate.delete(REFRESH_KEY_PREFIX + ":" + username);
            redisTemplate.delete(ACCESS_KEY_PREFIX + ":" + username);
            log.info("all tokens has removed from the redis");
        }

       public void isAccessTokenValid(String username) {
            String key = ACCESS_KEY_PREFIX + ":" + username;
            String value = redisTemplate.opsForValue().get(key);
            if(value == null) {
                throw new TokenHasExpiredException("token has expired." +
                        "You need to authenticate again");
            }
       }
}