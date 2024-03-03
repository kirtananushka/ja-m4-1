package com.tananushka.module04.listener;

import com.tananushka.module04.entity.UserEntity;
import com.tananushka.module04.repository.UserRepository;
import com.tananushka.module04.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private LoginAttemptService loginAttemptService;
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        if (e.getAuthentication().getPrincipal() instanceof String username) {
            UserEntity user = userRepository.findByUserName(username);
            if (user != null) {
                loginAttemptService.loginFailed(username);
            }
        }
    }
}
