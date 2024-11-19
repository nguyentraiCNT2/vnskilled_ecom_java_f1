package vnskilled.edu.ecom.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisService {

    private static final String HASH_KEY = "TOKEN";

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private HashOperations<Object, String, String> hashOperations;

    @Autowired
    public RedisService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(String tokenId, String token) {
        hashOperations.put(HASH_KEY, tokenId, token);
    }

    public Map<String, String> findAll() {
        return hashOperations.entries(HASH_KEY);
    }

    public String findById(String tokenId) {
        return hashOperations.get(HASH_KEY, tokenId);
    }

    public void update(String tokenId, String token) {
        save(tokenId, token);
    }

    public void delete(String tokenId) {
        hashOperations.delete(HASH_KEY, tokenId);
    }
    public boolean isTokenExists(String token) {
        return hashOperations.values(HASH_KEY).contains(token);
    }

}
