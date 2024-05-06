package com.amalitechfileserver.fileserverbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SecretKey {
    private String secretKey;
}
