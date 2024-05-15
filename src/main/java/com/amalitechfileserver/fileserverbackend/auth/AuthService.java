package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.AuthDto;
import com.amalitechfileserver.fileserverbackend.exception.InputBlank;
import com.amalitechfileserver.fileserverbackend.exception.UserAlreadyRegisteredException;
import com.amalitechfileserver.fileserverbackend.exception.UserNotFound;

public interface AuthService {

    String register(AuthDto registerDto) throws UserAlreadyRegisteredException, InputBlank;

    AuthResponse login(AuthDto loginDto) throws InputBlank;

    String verifyAccount(String token);

    String forgotPassword(AuthDto forgotPasswordDto) throws UserNotFound;

    String updatePassword(String token, AuthDto updatePasswordDto) throws UserNotFound;
}
