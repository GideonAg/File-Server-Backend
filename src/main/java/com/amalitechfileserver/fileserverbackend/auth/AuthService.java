package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.AuthDto;
import com.amalitechfileserver.fileserverbackend.exception.UserAlreadyRegisteredException;

public interface AuthService {

    String register(AuthDto registerDto) throws UserAlreadyRegisteredException;

    AuthResponse login(AuthDto loginDto);

    String verifyAccount(String token);

    String forgotPassword(AuthDto forgotPasswordDto);

    String updatePassword(String token, AuthDto updatePasswordDto);
}
