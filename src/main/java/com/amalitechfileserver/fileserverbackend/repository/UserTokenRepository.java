package com.amalitechfileserver.fileserverbackend.repository;

import com.amalitechfileserver.fileserverbackend.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, String> {
    Optional<UserToken> findByToken(String token);
}
