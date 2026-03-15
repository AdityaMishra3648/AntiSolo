package com.AntiSolo.AntiSolo.Services;

//import jakarta.mail.internet.MimeMessage;
import com.resend.services.emails.model.SendEmailResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import jakarta.mail.MessagingException;


import com.resend.Resend;
//import com.resend.services.emails.model.CreateEmailOptions;
//import com.resend.services.emails.model.CreateEmailResponse;
import com.resend.services.emails.model.SendEmailRequest;

import java.util.Random;

@Service
public class EmailService {
//    @Autowired
//    private JavaMailSender javaMailSender;
    @Autowired
    private OTPService otpService;

    @Value("${resend.api.key}")
    private String apiKey;

    private Resend resend;

    @PostConstruct
    public void init() {
        resend = new Resend(apiKey);
    }

    public void sendWarningEmail(String toEmail, String userName, String projectTitle) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setTo(toEmail);
//            helper.setSubject("⚠️ Warning: Inappropriate Content in Your Project Opening Post on AntiSolo");

            String htmlContent = """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <div style="padding: 20px; border: 1px solid #ddd; border-radius: 8px; max-width: 600px; margin: auto; background-color: #f9f9f9;">
                        <h2 style="color: #c0392b;">Important Notice Regarding Your Recent Project Post</h2>
                        <p>Dear %s,</p>
                        <p>We hope you're enjoying your experience on <strong>AntiSolo</strong>.</p>
                        <p>This is to inform you that your recent project post with Id <strong>\"%s\"</strong> has been reported by users for containing content that may be misleading, inappropriate, spam or irrelevant.</p>
                        <p>After a review by our moderation team, we found merit in these reports.</p>
                        <p><strong>This serves as a formal warning</strong>. Please make the necessary corrections or remove the post within the next 24 hours. Repeated violations may result in restricted access or account suspension.</p>
                        <p>Thank you for being a part of the AntiSolo community.</p>
                        <br/>
                        <p style="color: #888;">- AntiSolo Admin Team</p>
                    </div>
                </body>
                </html>
                """.formatted(userName, projectTitle);


        SendEmailRequest request = SendEmailRequest.builder()
                .from("AntiSolo <support@antisolo.co.in>")
                .to(toEmail)
                .subject("⚠ Warning: Inappropriate Content in Your Project Opening Post on AntiSolo")
                .html(htmlContent)
                .build();

        SendEmailResponse data = resend.emails().send(request);
//            helper.setText(htmlContent, true); // "true" means it's HTML

//            javaMailSender.send(message);

//        } catch (MessagingException e) {
//            e.printStackTrace(); // Or handle error more gracefully
//        }
    }

    public void sendProfileWarningEmail(String toEmail, String userName) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setTo(toEmail);
//            helper.setSubject("⚠️ Warning: Inappropriate Content in Your Profile on AntiSolo");

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; color: #333;">
                <div style="padding: 20px; border: 1px solid #ddd; border-radius: 8px; max-width: 600px; margin: auto; background-color: #f9f9f9;">
                    <h2 style="color: #c0392b;">Warning: Profile Violation Detected</h2>
                    <p>Dear %s,</p>
                    <p>We hope you're having a good experience on <strong>AntiSolo</strong>.</p>
                    <p>Your profile has been reported by users for containing content that may be considered vulgar, controversial, or inappropriate for our platform.</p>
                    <p>After a review by our moderation team, we found that the report holds merit.</p>
                    <p><strong>This serves as a formal warning</strong>. Please update your profile and remove any offensive material within the next 24 hours to avoid further action.</p>
                    <p>Failure to comply may lead to restricted access or account suspension as per our community guidelines.</p>
                    <p>We appreciate your cooperation in maintaining a respectful and professional community.</p>
                    <br/>
                    <p style="color: #888;">- AntiSolo Admin Team</p>
                </div>
            </body>
            </html>
            """.formatted(userName);

        SendEmailRequest request = SendEmailRequest.builder()
                .from("AntiSolo <support@antisolo.co.in>")
                .to(toEmail)
                .subject("⚠ Warning: Inappropriate Content in Your Profile on AntiSolo")
                .html(htmlContent)
                .build();

        SendEmailResponse data = resend.emails().send(request);

//            helper.setText(htmlContent, true);
//            javaMailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace(); // You can handle it better with logging
//        }
    }


    public int sendMail(String toEmail){
        try {
            Random random = new Random();
            int otp = 100000 + random.nextInt(900000); // Generates a number between

            // Prepare the email
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setTo(toEmail);
//            helper.setSubject("🔐 Your AntiSolo Account Verification Code");

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05);">
                    <h2 style="color: #2c3e50;">AntiSolo Account Verification</h2>
                    <p>Hello,</p>
                    <p>Thank you for using <strong>AntiSolo</strong>. To complete your account verification, please use the following One-Time Password (OTP):</p>
                    <h1 style="letter-spacing: 4px; color: #e74c3c; text-align: center;">%d</h1>
                    <p>This code will expire in 15 minutes. Please do not share it with anyone for security reasons.</p>
                    <br>
                    <p style="color: #888;">— The AntiSolo Team</p>
                </div>
            </body>
            </html>
        """.formatted(otp);
            System.out.println("message created for Otp");
            SendEmailRequest request = SendEmailRequest.builder()
                    .from("AntiSolo <support@antisolo.co.in>")
                    .to(toEmail)
                    .subject("\uD83D\uDD10 Your AntiSolo Account Verification Code")
                    .html(htmlContent)
                    .build();

            SendEmailResponse data = resend.emails().send(request);
//            helper.setText(htmlContent, true);

//            javaMailSender.send(message);
            System.out.println("sent otp");
            otpService.generateAndSaveOTP(String.valueOf(otp), toEmail);

            return otp;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }


    }

    public boolean validateOTPWithMail(String email,String otp){
        return otpService.validateOTP(email,otp);
    }
}
