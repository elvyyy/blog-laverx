package by.laverx.teleblog.service;

import by.laverx.teleblog.dto.ArticleDto;
import by.laverx.teleblog.dto.SignUpInfo;
import by.laverx.teleblog.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    @PreAuthorize("isAnonymous()")
    UserDto create(SignUpInfo signUpInfo);

    @PreAuthorize("permitAll()")
    Optional<UserDto> findByEmail(String email);

    @PreAuthorize("isAnonymous()")
    void enableUser(long userId);

    @PreAuthorize("hasAuthority('USER') and @userSecurity.hasUserId(authentication, #userId)")
    Page<ArticleDto> findAll(long userId, Pageable pageable);

    @PreAuthorize("hasAuthority('USER')")
    Page<ArticleDto> findAll(Principal principal, Pageable pageable);
}
