package com.amalitechfileserver.fileserverbackend.repository;

import com.amalitechfileserver.fileserverbackend.entity.JwtOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<JwtOfUser, String> {
    Optional<JwtOfUser> findByToken(String jwt);
    List<JwtOfUser> findByUserId(String userId);
}
