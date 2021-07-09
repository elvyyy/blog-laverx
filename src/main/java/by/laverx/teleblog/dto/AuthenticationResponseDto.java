package by.laverx.teleblog.dto;

import by.laverx.teleblog.domain.mysql.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthenticationResponseDto {
    private Long id;
    private String email;
    private String token;
    private Collection<? extends Role> roles;
}
