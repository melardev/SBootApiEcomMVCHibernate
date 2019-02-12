package com.melardev.spring.shoppingcartweb.dtos.request;

import com.melardev.spring.shoppingcartweb.annotations.ConfirmationFormField;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@ConfirmationFormField(first = "password", second = "passwordConfirmation", message = "The password fields do not match")
public class RegisterDto {
    @Size(min = 4, max = 255, message = "{errors.username.username.size}")
    @NotNull(message = "{errors.username.username.null}")
    @NotEmpty(message = "{errors.username.username.empty}")
    private String username;


    @Email(message = "Email must be valid")
    @NotBlank
    @Size(max = 60)
    private String email;

    @Size(min = 2, max = 255)
    private
    String firstName;

    @Min(2)
    @Max(255)
    private
    String lastName;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Length(min = 4, max = 60, message = "password confirmation error")
    private String passswordConfirmation;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassswordConfirmation() {
        return passswordConfirmation;
    }

    public void setPassswordConfirmation(String passswordConfirmation) {
        this.passswordConfirmation = passswordConfirmation;
    }
}