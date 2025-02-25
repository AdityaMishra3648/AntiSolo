package com.AntiSolo.AntiSolo.Services;


import com.AntiSolo.AntiSolo.Entity.OTP;
import com.AntiSolo.AntiSolo.Repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OTPService {

    @Autowired
    private OTPRepository otpRepository;

    public void generateAndSaveOTP(String otpValue,String email) {
        OTP otp = new OTP(otpValue,email);
        otpRepository.save(otp);
    }

    public boolean validateOTP(String id,String otpValue) {
        Optional<OTP> otp = otpRepository.findById(id);
        if(!otp.isPresent())return false;
        return otp.get().getOtp().equals(otpValue);
//        OTP otp = otpRepository.findByOtp(otpValue);
        // return otp != null; // OTP is valid if found
    }
}