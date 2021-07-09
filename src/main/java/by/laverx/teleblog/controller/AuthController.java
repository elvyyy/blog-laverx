package by.laverx.teleblog.controller;

import by.laverx.teleblog.domain.redis.Jwt;
import by.laverx.teleblog.domain.redis.VerificationToken;
import by.laverx.teleblog.dto.AuthenticationRequestDto;
import by.laverx.teleblog.dto.AuthenticationResponseDto;
import by.laverx.teleblog.dto.SignUpInfo;
import by.laverx.teleblog.dto.UserDto;
import by.laverx.teleblog.exception.ErrorCode;
import by.laverx.teleblog.exception.IdentityVerificationException;
import by.laverx.teleblog.locale.LocaleTranslator;
import by.laverx.teleblog.service.JwtTokenProvider;
import by.laverx.teleblog.service.JwtWhitelistService;
import by.laverx.teleblog.service.UserService;
import by.laverx.teleblog.service.VerificationTokenService;
import by.laverx.teleblog.service.event.OnRegistrationCompleteEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtProvider;
    private final JwtWhitelistService jwtWhitelistService;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenService verificationTokenService;
    private final LocaleTranslator localeTranslator;

    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@Valid @RequestBody SignUpInfo signUpInfo) {
        UserDto userDto = userService.create(signUpInfo);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDto, null, null));
        return userDto;
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authenticate(@RequestBody @Valid AuthenticationRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        AuthenticationResponseDto response = userService.findByEmail(email)
                .map(user -> {
                    String token = jwtProvider.generateToken(user.getEmail(), user.getRoles());
                    jwtWhitelistService.save(jwtProvider.mapTokenToJwt(token));
                    return AuthenticationResponseDto.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .token(token)
                            .roles(user.getRoles())
                            .build();
                }).orElseGet(AuthenticationResponseDto::new);
        return response;
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        Jwt jwt = jwtProvider.mapTokenToJwt(token);
        jwtWhitelistService.invalidate(jwt);
    }

    @GetMapping("/confirm/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmSignUp(@PathVariable String token) {
        VerificationToken verificationToken = verificationTokenService.find(token)
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("verification.token.not-exist");
                    return new IdentityVerificationException(message, ErrorCode.VERIFICATION_FAILED.getErrorCode());
                });

        if (verificationToken.getInvalidated()) {
            String message = localeTranslator.toLocale("verification.token.invalid");
            throw new IdentityVerificationException(message, ErrorCode.VERIFICATION_FAILED.getErrorCode());
        }

        Long userId = verificationToken.getUserId();
        userService.enableUser(userId);
    }

}