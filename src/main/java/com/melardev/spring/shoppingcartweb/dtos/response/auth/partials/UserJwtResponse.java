package com.melardev.spring.shoppingcartweb.dtos.response.auth.partials;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserJwtResponse {
    private final String username;
    private final String email;
    private final String token;
    private final List<String> roles;

    public UserJwtResponse(String username, String email, List<String> authorities, String token) {
        this.token = token;
        this.username = username;
        this.email = email;
        roles = authorities;

    }

    public static UserJwtResponse build(String username, String email, Collection<? extends GrantedAuthority> authorities, String jwt) {
        List<String> auths = new ArrayList<>();
        authorities.forEach(a -> auths.add(a.getAuthority().toString()));
        return new UserJwtResponse(username, email, auths, jwt);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getToken() {
        return token;
    }
}
