package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.domain.redis.VerificationToken;
import by.laverx.teleblog.repository.redis.VerificationTokenRepository;
import by.laverx.teleblog.service.VerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public VerificationToken save(VerificationToken token) {
        return tokenRepository.save(token);
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public boolean exists(String token) {
        return tokenRepository.existsByToken(token);
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public Optional<VerificationToken> find(String token) {
        return tokenRepository.findByToken(token);
    }
}
