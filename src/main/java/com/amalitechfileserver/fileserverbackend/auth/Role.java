package com.amalitechfileserver.fileserverbackend.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static com.amalitechfileserver.fileserverbackend.auth.Permissions.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(List.of(
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_WRITE,
            ADMIN_DELETE
    )),
    USER(new ArrayList<>());

    @Getter
    private final List<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream().
                map(permissions1 -> new SimpleGrantedAuthority(permissions1.getPermissions()))
                .toList();
    }

}
