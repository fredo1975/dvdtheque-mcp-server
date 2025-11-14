package fr.bluechipit.dvdtheque_mcp_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public Mono<Map<String, Object>> getUserProfile(Mono<Authentication> authenticationMono) {
        return authenticationMono.map(authentication -> {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            return Map.of(
                    "username", jwt.getClaim("preferred_username"),
                    "email", jwt.getClaim("email") != null ? jwt.getClaim("email") : "N/A",
                    "name", jwt.getClaim("name") != null ? jwt.getClaim("name") : "N/A",
                    "roles", authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()),
                    "message", "User profile retrieved successfully (Reactive)",
                    "sub", jwt.getSubject()
            );
        });
    }
}
