package by.laverx.teleblog.domain.redis;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@RedisHash("Jwt")
public class Jwt implements Serializable {
    @Id
    private String id;

    @Indexed
    private String subject;

    private Boolean invalidated;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long timeToLive;

}
