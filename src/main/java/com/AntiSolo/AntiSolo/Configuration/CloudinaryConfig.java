package com.AntiSolo.AntiSolo.Configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary(){
        Map config = new HashMap<>();
        config.put("cloud_name","dx7bcuxjn");
        config.put("api_key","613864729513194");
        config.put("api_secret","954sbj6tRTzUJpUCHUaGRZTyuSU");
        config.put("secure",true);
        return new Cloudinary(config);
    }
}
