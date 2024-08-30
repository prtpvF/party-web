package by.intexsoft.diplom.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

        private final RedisTemplate<String, String> redisTemplate;
        private final String ACCESS_KEY_PREFIX = "access_token";

        public String isAccessTokenValid(String username) {
            String key = ACCESS_KEY_PREFIX + ":" + username;
            return redisTemplate.opsForValue().get(key);
        }
}
