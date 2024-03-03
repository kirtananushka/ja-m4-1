package com.tananushka.module04.controller;

import com.tananushka.module04.model.BlockedUser;
import com.tananushka.module04.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static com.tananushka.module04.securityconfig.CustomAuthenticationFailureHandler.LAST_USERNAME_KEY;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainSecurityController {

    private final LoginAttemptService loginAttemptService;

    @GetMapping("/")
    public String index() {
        logUserInfo("/");
        return "index";
    }

    @GetMapping("/info")
    public String info() {
        logUserInfo("/info");
        return "info";
    }

    @GetMapping("/about")
    public String about() {
        logUserInfo("/about");
        return "about";
    }

    @GetMapping("/admin")
    public String admin() {
        logUserInfo("/admin");
        return "admin";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", defaultValue = "false") boolean loginError,
                        ModelMap model, HttpSession session) {
        blockIfTooManyFailedAttempts(loginError, model, session);
        return "login";
    }

    @GetMapping("/logoutSuccess")
    public String logoutSuccess() {
        return "logout-success";
    }

    @GetMapping("/blocked")
    public String showBlockedUsers(Model model) {
        List<BlockedUser> blockedUsers = loginAttemptService.retrieveBlockedUsers();
        if (!blockedUsers.isEmpty()) {
            model.addAttribute("blockedUsers", blockedUsers);
        }
        return "blocked";
    }

    private void logUserInfo(String page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        log.info("Page '{}' accessed by: {} with roles: {}", page, currentPrincipalName, roles);
    }

    private void blockIfTooManyFailedAttempts(boolean loginError, ModelMap model, HttpSession session) {
        String userName = getUserName(session);
        if (loginError && userName != null && (loginAttemptService.isBlocked(userName))) {
            log.info("User {} is blocked", userName);
            model.addAttribute("accountLocked", Boolean.TRUE);
        }
    }

    private String getUserName(HttpSession session) {
        final String username = (String) session.getAttribute(LAST_USERNAME_KEY);
        if (username != null && !username.isEmpty()) {
            session.removeAttribute(LAST_USERNAME_KEY);
        }
        return username;
    }
}

