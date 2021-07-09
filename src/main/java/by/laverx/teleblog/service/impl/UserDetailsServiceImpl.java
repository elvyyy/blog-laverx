package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.domain.mysql.Role;
import by.laverx.teleblog.repository.mysql.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> {
                    return User.builder()
                            .username(user.getEmail())
                            .password(new String(user.getPassword()))
                            .authorities(mapUserRolesToAuthorities(user.getRoles()))
                            .disabled(user.getDisabled())
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    private Collection<? extends GrantedAuthority> mapUserRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
