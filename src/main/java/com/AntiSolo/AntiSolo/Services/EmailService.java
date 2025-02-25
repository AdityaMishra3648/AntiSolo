package com.AntiSolo.AntiSolo.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private OTPService otpService;
    public int sendMail(String to){
        try {
            System.out.println("sending mail to "+to);
            Random random = new Random();
            int randomNumber = 100000 + random.nextInt(900000); // Generates a number between
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject("OTP from myapp is "+String.valueOf(randomNumber));
            mail.setText("Sample body for my message is the OTP "+String.valueOf(randomNumber));
            System.out.println("invalid mail sending 1 "+to+" body   = "+"Sample body for my message is the OTP "+String.valueOf(randomNumber));

            javaMailSender.send(mail);
            otpService.generateAndSaveOTP(String.valueOf(randomNumber),to);
            return randomNumber;
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(to);
//            helper.setSubject("OTP from app");
//            helper.setText(body, true);
//            helper.setFrom("your-email@gmail.com");

//            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
//            throw new RuntimeException();
            return 0;
        }

    }

    public boolean validateOTPWithMail(String email,String otp){
        return otpService.validateOTP(email,otp);
    }
}
