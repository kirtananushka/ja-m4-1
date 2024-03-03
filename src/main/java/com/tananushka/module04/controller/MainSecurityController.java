package com.tananushka.module04.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
@Slf4j
public class MainSecurityController {

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

    private void logUserInfo(String page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        log.info("Page '{}' accessed by: {} with roles: {}", page, currentPrincipalName, roles);
    }
}

