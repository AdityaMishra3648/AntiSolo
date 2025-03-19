package com.AntiSolo.AntiSolo.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor{

    @Autowired
    JwtHelper jwtHelper;

}