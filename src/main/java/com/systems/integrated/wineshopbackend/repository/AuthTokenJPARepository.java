package com.systems.integrated.wineshopbackend.repository;

import com.systems.integrated.wineshopbackend.models.users.AuthToken;
import com.systems.integrated.wineshopbackend.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenJPARepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findAuthTokenByUser(User user);

    Optional<AuthToken> findAuthTokenByToken(String token);
}
