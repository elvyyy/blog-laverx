package by.laverx.teleblog.repository.redis;

import by.laverx.teleblog.domain.redis.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, String> {
}
