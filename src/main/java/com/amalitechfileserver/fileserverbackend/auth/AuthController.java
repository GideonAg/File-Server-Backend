package com.amalitechfileserver.fileserverbackend.auth;

import com.amalitechfileserver.fileserverbackend.dto.AuthDto;
import com.amalitechfileserver.fileserverbackend.exception.UserAlreadyRegisteredException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthDto registerDto) throws UserAlreadyRegisteredException {
        return ResponseEntity.ok(authService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @GetMapping("/register/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.verifyAccount(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody AuthDto forgotPasswordDto) {
        return ResponseEntity.ok(authService.forgotPassword(forgotPasswordDto));
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestParam("token") String token, @RequestBody AuthDto updatePasswordDto)
    {
        return ResponseEntity.ok(authService.updatePassword(token, updatePasswordDto));
    }

}
