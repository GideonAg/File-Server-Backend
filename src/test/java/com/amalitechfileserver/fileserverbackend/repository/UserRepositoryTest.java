package com.amalitechfileserver.fileserverbackend.repository;

import com.amalitechfileserver.fileserverbackend.auth.Role;
import com.amalitechfileserver.fileserverbackend.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_Save_ReturnSavedUser() {

        UserEntity user = UserEntity.builder()
                .email("user@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();

        UserEntity savedUser = userRepository.save(user);
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotBlank();
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUser() {

        UserEntity user = UserEntity.builder()
                .email("userOne@gmail.com")
                .password("password")
                .role(Role.USER)
                .build();

        userRepository.save(user);
        Optional<UserEntity> fetchedUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertThat(fetchedUser.isPresent()).isEqualTo(true);
    }
}
