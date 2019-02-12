package com.melardev.spring.shoppingcartweb.dtos.response;

import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsDto extends SuccessResponse {
    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<String> roles;

    public UserDetailsDto(Long id, String username, String firstName, String lastName, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public static UserDetailsDto build(User user) {
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(a -> roles.add(a.getName()));
        return new UserDetailsDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), roles);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

}
