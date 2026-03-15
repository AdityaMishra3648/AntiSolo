package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends MongoRepository<OTP, String> {
    Optional<OTP> findByOtp(String otp); // Find OTP by value
}
