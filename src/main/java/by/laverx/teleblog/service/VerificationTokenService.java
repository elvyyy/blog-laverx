package by.laverx.teleblog.service;

import by.laverx.teleblog.domain.redis.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    VerificationToken save(VerificationToken token);

    boolean exists(String token);

    Optional<VerificationToken> find(String token);
}
