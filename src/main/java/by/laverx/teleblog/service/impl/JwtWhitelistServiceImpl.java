package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.domain.redis.Jwt;
import by.laverx.teleblog.repository.redis.JwtRepository;
import by.laverx.teleblog.service.JwtWhitelistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class JwtWhitelistServiceImpl implements JwtWhitelistService {
    private final JwtRepository jwtRepository;

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public Jwt save(Jwt jwt) {
        jwt.setInvalidated(false);
        return jwtRepository.save(jwt);
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public boolean invalidate(Jwt jwt) {
        jwt.setInvalidated(true);
        return jwtRepository.save(jwt).getInvalidated();
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public boolean isInvalidated(Jwt jwt) {
        String id = jwt.getId();
        return jwtRepository.findById(id)
                .map(Jwt::getInvalidated)
                .orElse(true);
    }
}
