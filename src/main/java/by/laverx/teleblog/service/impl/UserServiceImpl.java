package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.domain.mysql.Article;
import by.laverx.teleblog.domain.mysql.Role;
import by.laverx.teleblog.domain.mysql.User;
import by.laverx.teleblog.dto.ArticleDto;
import by.laverx.teleblog.dto.SignUpInfo;
import by.laverx.teleblog.dto.UserDto;
import by.laverx.teleblog.exception.EntityExistsException;
import by.laverx.teleblog.exception.EntityNotFoundException;
import by.laverx.teleblog.exception.ErrorCode;
import by.laverx.teleblog.locale.LocaleTranslator;
import by.laverx.teleblog.repository.mysql.ArticleRepository;
import by.laverx.teleblog.repository.mysql.RoleRepository;
import by.laverx.teleblog.repository.mysql.UserRepository;
import by.laverx.teleblog.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ArticleRepository articleRepository;
    private final LocaleTranslator localeTranslator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto create(SignUpInfo signUpInfo) {
        if (userRepository.existsByEmail(signUpInfo.getEmail())) {
            String message = localeTranslator.toLocale("message.entity.email.exists");
            throw new EntityExistsException(message, ErrorCode.EMAIL_EXISTS.getErrorCode());
        }

        User user = encodeCredentialsAndMapToUser(signUpInfo);
        user.setCreatedAt(LocalDateTime.now());
        User createdUser = userRepository.saveAndFlush(user);
        return modelMapper.map(createdUser, UserDto.class);
    }

    private User encodeCredentialsAndMapToUser(SignUpInfo signUpInfo) {
        User user = modelMapper.map(signUpInfo, User.class);
        String encodedPassword = passwordEncoder.encode(signUpInfo.getPassword());
        user.setPassword(encodedPassword.getBytes(StandardCharsets.US_ASCII));
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    @Transactional
    public void enableUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = localeTranslator.toLocale("entity.user.not-exist");
                    return new EntityNotFoundException(message, ErrorCode.ENTITY_NOT_FOUND.getErrorCode());
                });
        user.setDisabled(false);
        Role roleUser = roleRepository.findByName("USER");
        user.getRoles().add(roleUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleDto> findAll(long userId, Pageable pageable) {
        return articleRepository.findByUser_Id(userId, pageable)
                .map(article -> modelMapper.map(article, ArticleDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleDto> findAll(Principal principal, Pageable pageable) {
        String email = principal.getName();
        Long userId = userRepository.findByEmail(email)
                .map(User::getId)
                .get();
        return findAll(userId, pageable);
    }
}
