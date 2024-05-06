package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.config.JwtService;
import com.amalitechfileserver.fileserverbackend.dto.AuthDto;
import com.amalitechfileserver.fileserverbackend.entity.JwtOfUser;
import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserToken;
import com.amalitechfileserver.fileserverbackend.exception.UserAlreadyRegisteredException;
import com.amalitechfileserver.fileserverbackend.repository.JwtRepository;
import com.amalitechfileserver.fileserverbackend.repository.UserRepository;
import com.amalitechfileserver.fileserverbackend.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SendMails sendMails;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;
    private final JwtRepository jwtRepository;
    private final UserTokenRepository userTokenRepository;

    @Override
    public String register(AuthDto registerDto) throws UserAlreadyRegisteredException {
        Optional<UserEntity> fetchedUser = userRepository.findByEmail(registerDto.getEmail());
        if (fetchedUser.isPresent())
            throw new UserAlreadyRegisteredException("User is already registered");

        UserEntity new_user = UserEntity.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.ADMIN)
                .build();

        UserEntity savedUser = userRepository.save(new_user);
        sendMails.sendVerificationEmail(savedUser);
        return "Account verification link sent to email.";
    }

    @Override
    public AuthResponse login(AuthDto loginDto) {
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        UserEntity user = userRepository.findByEmail(loginDto.getEmail()).get();
        String jwt = jwtService.generateJwt(user);
        saveUserWithJwt(user, jwt);

        return AuthResponse.builder()
                .userEmail(user.getEmail())
                .userId(user.getId())
                .jwt(jwt)
                .build();
    }

    @Override
    public String verifyAccount(String token) {
        Optional<UserToken> verifyToken = userTokenRepository.findByToken(token);

        if (verifyToken.isPresent()) {
            UserEntity user = verifyToken.get().getUser();
            user.setEnabled(true);
            userRepository.save(user);
            userTokenRepository.delete(verifyToken.get());
            return "Account verification successful";
        };

        return "Account already verified";
    }

    @Override
    public String forgotPassword(AuthDto forgotPasswordDto) {
        UserEntity user = userRepository.findByEmail(forgotPasswordDto.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("Incorrect email address")
        );

        sendMails.sendPasswordResetEmail(user);
        return "Password reset link sent to email";
    }

    @Override
    public String updatePassword(String token, AuthDto updatePasswordDto) {
        UserToken userToken = userTokenRepository.findByToken(token).orElseThrow(
                () -> new UsernameNotFoundException("Password update failed")
        );

        UserEntity user = userToken.getUser();
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        userRepository.save(user);
        userTokenRepository.delete(userToken);
        return "Password updated successfully";
    }

    private void saveUserWithJwt(UserEntity user, String jwt) {
        JwtOfUser jwtOfUser = JwtOfUser.builder()
                .user(user)
                .token(jwt)
                .build();

        jwtRepository.save(jwtOfUser);
    }

}
