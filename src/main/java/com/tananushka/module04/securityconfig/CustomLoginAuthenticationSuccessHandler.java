package com.tananushka.module04.securityconfig;

import com.tananushka.module04.entity.UserEntity;
import com.tananushka.module04.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws ServletException, IOException {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        loginAttemptService.loginSucceeded(user.getUsername());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
