package com.tananushka.module04.service;

import com.tananushka.module04.entity.UserEntity;
import com.tananushka.module04.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username);
        if (user == null) {
            log.error("UserEntity with username={} not found!", username);
            throw new UsernameNotFoundException("UserEntity not found!");
        } else {
            if (loginAttemptService.isBlocked(user.getUsername())) {
                throw new LockedException("User is blocked");
            }
        }
        log.info("UserEntity found: " + user);
        return user;
    }
}
