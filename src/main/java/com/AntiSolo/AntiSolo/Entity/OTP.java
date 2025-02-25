package com.AntiSolo.AntiSolo.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "otps") // Collection name in MongoDB
public class OTP {

    @Id
    private String id;  // Unique ID

    private String otp; // The OTP value

    @Indexed(expireAfterSeconds = 900) // Set expiry time (900 sec = 15 min)
    private Instant createdAt;  // Timestamp when OTP is created

    public OTP(String otp,String id) {
        this.id = id;
        this.otp = otp;
        this.createdAt = Instant.now(); // Set the current time
    }
    public String getId(){
        return this.id;
    }

    // Getters and setters
    public String getOtp() {
        return otp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
