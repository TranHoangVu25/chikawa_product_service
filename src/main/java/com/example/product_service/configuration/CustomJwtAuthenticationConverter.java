package com.example.product_service.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter {

    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles"); // claim lưu roles trong JWT
        if (roles == null) roles = List.of();

        return roles.stream()
                .map(SimpleGrantedAuthority::new) // dùng hasAuthority("customer") hoặc hasAuthority("admin")
                .collect(Collectors.toList());
    }
}
