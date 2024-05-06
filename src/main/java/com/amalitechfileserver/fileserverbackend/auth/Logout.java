package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.entity.JwtOfUser;
import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import com.amalitechfileserver.fileserverbackend.repository.JwtRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Logout implements LogoutHandler {

    private final JwtRepository jwtRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        String jwt = authHeader.substring(7);
        jwtRepository.findByToken(jwt).ifPresent(
                item -> deleteAllJwtOfUser(item.getUser())
        );
    }

    private void deleteAllJwtOfUser(UserEntity user) {
        List<JwtOfUser> tokens = jwtRepository.findByUserId(user.getId());
        if (tokens.isEmpty())
            return;
        jwtRepository.deleteAll(tokens);
    }
}
