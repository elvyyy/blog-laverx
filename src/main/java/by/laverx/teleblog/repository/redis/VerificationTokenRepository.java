package by.laverx.teleblog.repository.redis;

import by.laverx.teleblog.domain.redis.VerificationToken;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends KeyValueRepository<VerificationToken, String> {

    boolean existsByToken(String token);

    Optional<VerificationToken> findByToken(String token);
}
