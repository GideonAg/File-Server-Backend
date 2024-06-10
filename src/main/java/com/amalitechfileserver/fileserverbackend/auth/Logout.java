package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserToken;
import com.amalitechfileserver.fileserverbackend.repository.UserTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class Logout implements LogoutHandler {

    private final UserTokenRepository userTokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        String jwt = authHeader.substring(7);
        Optional<UserToken> token = userTokenRepository.findByToken(jwt);

        if (token.isPresent()) {
            invalidateAllTokens(token.get().getUser());
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), "Logout success");
            } catch (IOException e) {
                throw new RuntimeException("Failed to logout");
            }
        } else {
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), "Failed to logout");
            } catch (IOException e) {
                throw new RuntimeException("Failed to logout");
            }
        }
    }

    private void invalidateAllTokens(UserEntity user) {
        List<UserToken> allTokensByUser = userTokenRepository.findAllTokensByUser(user.getId());
        userTokenRepository.deleteAll(allTokensByUser);
    }
}
