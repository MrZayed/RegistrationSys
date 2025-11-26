package com.example.RegistrationSys.service;

import com.example.RegistrationSys.entity.User;
import jakarta.persistence.Cacheable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OtpService {

    private final Map<String, OtpEntry> otpCache = new HashMap<>();
//    @Autowired
//    private StringRedisTemplate redisTemplate;
    private final Random random = new Random();
    @Autowired private EmailService emailService;

    public String generateAndStoreOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
//        redisTemplate.opsForValue().set(email, otp, 5, TimeUnit.MINUTES); // 5 min expiry
        otpCache.put(email, new OtpEntry(otp, LocalDateTime.now().plusMinutes(5)));
        System.out.println("Generated OTP for " + email + ": " + otp);
        return otp; // return it so the controller can send it via email
    }

    public boolean validateOtp(String email, String otp) {
//        String storedOtp = redisTemplate.opsForValue().get(email);
//        if (storedOtp != null && storedOtp.equals(otp)) {
//            redisTemplate.delete(email); // clear after success
//            return true;
//        }
        OtpEntry entry = otpCache.get(email);

        if (entry == null) {
            return false;
        }

        // expired?
        if (entry.getExpiry().isBefore(LocalDateTime.now())) {
            otpCache.remove(email);
            return false;
        }

        // match?
        if (entry.getOtp().equals(otp)) {
            otpCache.remove(email); // use once
            return true;
        }

        return false;
    }

    @Data
    @AllArgsConstructor
    private static class OtpEntry {
        private final String otp;
        private final LocalDateTime expiry;

    }
}
