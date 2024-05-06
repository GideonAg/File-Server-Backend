package com.amalitechfileserver.fileserverbackend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthDto {
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
