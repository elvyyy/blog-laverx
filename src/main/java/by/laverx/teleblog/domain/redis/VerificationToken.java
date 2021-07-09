package by.laverx.teleblog.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@RedisHash("VerificationToken")
public class VerificationToken {
    @Id
    private String id;

    @Indexed
    private String token;

    private Long userId;

    private Boolean invalidated;

    @TimeToLive(unit = TimeUnit.HOURS)
    private Long timeToLive;
}
