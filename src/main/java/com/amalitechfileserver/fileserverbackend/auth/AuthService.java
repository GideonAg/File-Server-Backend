package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.AuthDto;
import com.amalitechfileserver.fileserverbackend.dto.ForgotPasswordDto;
import com.amalitechfileserver.fileserverbackend.dto.PasswordUpdateDto;
import com.amalitechfileserver.fileserverbackend.exception.UserAlreadyRegisteredException;
import com.amalitechfileserver.fileserverbackend.exception.UserNotFound;

public interface AuthService {

    String register(AuthDto registerDto) throws UserAlreadyRegisteredException;

    AuthResponse login(AuthDto loginDto);

    String verifyAccount(String token);

    String forgotPassword(ForgotPasswordDto forgotPasswordDto) throws UserNotFound;

    String updatePassword(String token, PasswordUpdateDto updatePasswordDto) throws UserNotFound;
}
