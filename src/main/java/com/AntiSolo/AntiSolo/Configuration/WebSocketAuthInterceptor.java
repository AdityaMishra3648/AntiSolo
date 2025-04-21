package com.AntiSolo.AntiSolo.Configuration;

import com.AntiSolo.AntiSolo.HelperEntities.Member;
import com.AntiSolo.AntiSolo.Services.ProjectService;
import com.AntiSolo.AntiSolo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.socket.WebSocketHandler;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor  {
    private final JwtHelper jwtHelper; // Your JWT helper class
    private final UserService userService;
    private final ProjectService projectService;
    public WebSocketAuthInterceptor(JwtHelper jwtHelper,UserService userService,ProjectService projectService) {
        this.jwtHelper = jwtHelper;
        this.userService = userService;
        this.projectService = projectService;
    }

    public static String[] extractUsernames(String chatId) {
        // Extract the part between "/user/" and "/queue/private"
        String trimmed = chatId.replaceAll("^/user/|/queue/private$", "");
        return trimmed.split("\\$"); // Split using '$' separator
    }


    public static String extractProjectId(String destination) {
        // Extract the part between "/user/" and "/queue/private"
        return destination.substring(7);
    }


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        System.out.println("websocket preSend called");

        String t = null;
        // Check if this is a CONNECT frame (initial WebSocket connection)
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = null;

            // Try getting token from session attributes
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                token = (String) sessionAttributes.get("jwtToken");
            }

            // If token not found in session attributes, try getting from headers
            if (token == null) {
                token = accessor.getFirstNativeHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7); // Remove "Bearer " prefix
                }
            }

            if (token != null && !jwtHelper.isTokenExpired(token)) {

                // Validate token expiry
                t = token;
//                System.out.println("t ko "+t+" bana diye hain theek hai naa");
                String username = jwtHelper.getUsernameFromToken(token);

                if (username != null) {
                    // Manually create UserDetails object (assuming no extra roles are needed)
                    UserDetails userDetails = new User(username, "", Collections.emptyList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Attach user to WebSocket session
                    accessor.setUser(authentication);
                }
            }
        }
        System.out.println(" path = "+accessor.getDestination());
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) && accessor.getDestination().startsWith("/group")) {
            // Validate subscription
//            Principal user = accessor.getUser();
            String token = null;

            // Try getting token from session attributes
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                token = (String) sessionAttributes.get("jwtToken");
            }

//             If token not found in session attributes, try getting from headers
            if (token == null) {
                token = accessor.getFirstNativeHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7); // Remove "Bearer " prefix
                }
            }
//            System.out.println("token null kyu hai "+token);
            if (token == null) {
                throw new RuntimeException("User not authenticated!");
            }
            String username = jwtHelper.getUsernameFromToken(token);
            Optional<com.AntiSolo.AntiSolo.Entity.User> user = userService.getById(username);

            if (user.isEmpty()) {
                throw new RuntimeException("User not authenticated!");
            }

            String destination = accessor.getDestination();
            if(destination==null)throw new RuntimeException("Unauthorized subscription!");
            String projectId = extractProjectId(destination);
//            System.out.println(" user1 = "+users[0]+" user2 = "+users[1]);
//            if(users.length!=2)throw new RuntimeException("Requires exactly two users");
//            String expectedDestination = "/user/" + user.get().getUserName() + "/queue/private";

//            if (!destination.equals(expectedDestination)) {
//                throw new RuntimeException("Unauthorized subscription!");
//            }
//            if(!user.get().getUserName().equals(users[0]) && !user.get().getUserName().equals(users[1]))throw new RuntimeException("Unauthorized User");
            boolean flag = !projectService.checkMember(user.get(),projectId);
//            System.out.println(projectId+" gave flag = "+flag);
//            for(Member buddy:user.get().getBuddies()){
//                if(buddy.getName().equals(users[0]) || buddy.getName().equals(users[1]))flag = false;
//            }
            if(flag)throw new RuntimeException("Cannot subscribe to non frined's path");

        }
        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            // Validate subscription
//            Principal user = accessor.getUser();
            String token = null;

            // Try getting token from session attributes
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                token = (String) sessionAttributes.get("jwtToken");
            }

//             If token not found in session attributes, try getting from headers
            if (token == null) {
                token = accessor.getFirstNativeHeader("Authorization");
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7); // Remove "Bearer " prefix
                }
            }
//            System.out.println("token null kyu hai "+token);
            if (token == null) {
                throw new RuntimeException("User not authenticated!");
            }
            String username = jwtHelper.getUsernameFromToken(token);
            Optional<com.AntiSolo.AntiSolo.Entity.User> user = userService.getById(username);

            if (user.isEmpty()) {
                throw new RuntimeException("User not authenticated!");
            }

            String destination = accessor.getDestination();
            if(destination==null)throw new RuntimeException("Unauthorized subscription!");
            String[] users = extractUsernames(destination);
//            System.out.println(" user1 = "+users[0]+" user2 = "+users[1]);
            if(users.length!=2)throw new RuntimeException("Requires exactly two users");
//            String expectedDestination = "/user/" + user.get().getUserName() + "/queue/private";

//            if (!destination.equals(expectedDestination)) {
//                throw new RuntimeException("Unauthorized subscription!");
//            }
            if(!user.get().getUserName().equals(users[0]) && !user.get().getUserName().equals(users[1]))throw new RuntimeException("Unauthorized User");
            boolean flag = true;
            for(Member buddy:user.get().getBuddies()){
                if(buddy.getName().equals(users[0]) || buddy.getName().equals(users[1]))flag = false;
            }
            if(flag)throw new RuntimeException("Cannot subscribe to non frined's path");

        }

        return message;
    }
    //    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        System.out.println("WebSocket Message Received: ");
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//        String destination = headerAccessor.getDestination(); // Get the path
//        String senderUsername = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : null;
//
//        // Implement logic to check if the sender is allowed to send messages
//        if (senderUsername == null || !isAllowedToSendMessage(senderUsername, destination)) {
//            throw new RuntimeException("Unauthorized to send message");
//        }
//
//        return message;
//    }
//
    private boolean isAllowedToSendMessage(String sender, String destination) {
        // Check if sender is in recipient's friend list (implement database check here)
        return true; // Replace with actual logic
    }

}