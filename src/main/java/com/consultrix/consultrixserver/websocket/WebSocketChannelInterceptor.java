package com.consultrix.consultrixserver.websocket;

import com.consultrix.consultrixserver.security.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    public WebSocketChannelInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtils.isTokenValid(token)) {
                    Claims claims = jwtUtils.decodeJWT(token);
                    String userId = claims.getId();
                    String role = claims.get("role", String.class);
                    List<SimpleGrantedAuthority> authorities = (role != null)
                            ? List.of(new SimpleGrantedAuthority(role))
                            : List.of();
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);
                    accessor.setUser(auth);
                }
            }
        }

        return message;
    }
}
