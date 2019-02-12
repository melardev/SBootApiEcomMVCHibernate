package com.melardev.spring.shoppingcartweb.dtos.response.users;

import com.melardev.spring.shoppingcartweb.models.User;

import javax.validation.constraints.*;

public class UserDto {


    @Size(min = 4, max = 255, message = "{errors.username.username.size}")
    @NotNull(message = "{errors.username.username.null}")
    @NotEmpty(message = "{errors.username.username.empty}")
    private String username;

    private Long id;
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 2, max = 255)
    String firstName;

    @Min(2)
    @Max(255)
    String lastName;

    public UserDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public static UserDto build(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}