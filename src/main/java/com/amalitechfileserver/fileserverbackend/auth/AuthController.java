package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.AuthDto;
import com.amalitechfileserver.fileserverbackend.dto.ForgotPasswordDto;
import com.amalitechfileserver.fileserverbackend.dto.PasswordUpdateDto;
import com.amalitechfileserver.fileserverbackend.exception.UserAlreadyRegisteredException;
import com.amalitechfileserver.fileserverbackend.exception.UserNotFound;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AuthDto registerDto)
            throws UserAlreadyRegisteredException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @GetMapping("/register/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.verifyAccount(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto)
            throws UserNotFound
    {
        return ResponseEntity.ok(authService.forgotPassword(forgotPasswordDto));
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestParam("token") String token, @RequestBody @Valid PasswordUpdateDto updatePasswordDto) throws UserNotFound {
        return ResponseEntity.ok(authService.updatePassword(token, updatePasswordDto));
    }

}
