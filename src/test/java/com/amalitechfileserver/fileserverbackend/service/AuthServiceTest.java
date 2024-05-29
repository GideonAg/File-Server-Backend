package com.amalitechfileserver.fileserverbackend.service;

import com.amalitechfileserver.fileserverbackend.auth.AuthResponse;
import com.amalitechfileserver.fileserverbackend.auth.AuthServiceImpl;
import com.amalitechfileserver.fileserverbackend.auth.Role;
import com.amalitechfileserver.fileserverbackend.auth.SendMails;
import com.amalitechfileserver.fileserverbackend.config.JwtService;
import com.amalitechfileserver.fileserverbackend.dto.AuthDto;
import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import com.amalitechfileserver.fileserverbackend.entity.UserToken;
import com.amalitechfileserver.fileserverbackend.exception.UserAlreadyRegisteredException;
import com.amalitechfileserver.fileserverbackend.exception.UserNotFound;
import com.amalitechfileserver.fileserverbackend.repository.UserRepository;
import com.amalitechfileserver.fileserverbackend.repository.UserTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private SendMails sendMails;

    private AuthDto authDto;
    private UserEntity user;
    private UserToken userToken;
    private String token;

    @BeforeEach
    public void init() {

        token = "token";
        authDto = AuthDto.builder()
                .email("user@gmail.com")
                .password("password")
                .build();

        user = UserEntity.builder()
                .email("user@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();

        userToken = UserToken.builder()
                .user(user)
                .token(token)
                .build();
    }

    @Test
    public void AuthService_Register_ReturnString()
            throws UserAlreadyRegisteredException {

        when(userRepository
                .findByEmail(authDto.getEmail())
        ).thenReturn(Optional.empty());

        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder
                .encode(authDto.getPassword())
        ).thenReturn("password");

        String response = authService.register(authDto);

        Assertions.assertThat(response).isNotBlank();
    }

    @Test
    public void AuthService_Login_ReturnObject() {

        Authentication authentication = Mockito.mock(Authentication.class);

        when(userRepository
                .findByEmail(authDto.getEmail())
        ).thenReturn(Optional.ofNullable(user));

        when(authenticationProvider
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))
        ).thenReturn(authentication);

        assert user != null;
        when(jwtService.generateJwt(user)).thenReturn("jwt");

        AuthResponse response = authService.login(authDto);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    public void AuthService_VerifyAccount_ReturnString() {

        when(userTokenRepository
                .findByToken(token)
        ).thenReturn(Optional.ofNullable(userToken));

        String response = authService.verifyAccount(token);

        Assertions.assertThat(response).isNotBlank();
    }

    @Test
    public void AuthService_ForgotPassword_ReturnString() throws UserNotFound {

        when(userRepository
                .findByEmail(authDto.getEmail())
        ).thenReturn(Optional.ofNullable(user));

        String response = authService.forgotPassword(authDto);

        Assertions.assertThat(response).isNotBlank();
    }

    @Test
    public void AuthService_UpdatedPassword_ReturnString() throws UserNotFound {

        when(userTokenRepository.findByToken(token)
        ).thenReturn(Optional.ofNullable(userToken));

        String response = authService.updatePassword(token, authDto);

        Assertions.assertThat(response).isNotBlank();
    }
}
