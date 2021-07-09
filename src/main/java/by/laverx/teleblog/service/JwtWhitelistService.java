package by.laverx.teleblog.service;

import by.laverx.teleblog.domain.redis.Jwt;

public interface JwtWhitelistService {
    Jwt save(Jwt jwt);

    boolean invalidate(Jwt jwt);

    boolean isInvalidated(Jwt jwt);
}
