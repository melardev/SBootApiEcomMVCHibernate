package com.melardev.spring.shoppingcartweb.dtos.response.users.partials;


import com.melardev.spring.shoppingcartweb.models.User;

public class UserIdAndUsernameDto {

    private String username;
    private Long id;

    public UserIdAndUsernameDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserIdAndUsernameDto build(User user) {
        return new UserIdAndUsernameDto(user.getId(), user.getUsername());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}