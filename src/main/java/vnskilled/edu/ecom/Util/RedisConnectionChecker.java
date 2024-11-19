package vnskilled.edu.ecom.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vnskilled.edu.ecom.Service.RedisService;

import javax.annotation.PostConstruct;

@Component
public class RedisConnectionChecker {

    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void checkConnection() {
        try {
            redisService.save("testKey", "testValue");
            Object value = redisService.findById("testKey");

            if ("testValue".equals(value)) {
                System.out.println("Kết nối đến Redis thành công!"+value);
            } else {
                System.out.println("Kết nối đến Redis thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi kết nối đến Redis: " + e.getMessage());
        }
    }
}
