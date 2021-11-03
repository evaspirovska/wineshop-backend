package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.users.AuthToken;

public interface AuthTokenService {
    AuthToken createAuthToken(Long userId, String type);

    AuthToken updateAuthToken(AuthToken authToken);

    boolean validateToken(String token);

    AuthToken findByUserId(Long userId);

    AuthToken findByToken(String token);
}
