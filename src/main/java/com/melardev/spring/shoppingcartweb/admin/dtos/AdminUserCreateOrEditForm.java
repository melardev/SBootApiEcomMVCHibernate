package com.melardev.spring.shoppingcartweb.admin.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melardev.spring.shoppingcartweb.admin.forms.RoleCreateEditDto;
import com.melardev.spring.shoppingcartweb.annotations.ConfirmationFormField;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@ConfirmationFormField(first = "password", second = "passwordConfirmation", message = "The password fields do not match")
public class AdminUserCreateOrEditForm {

    @Email(message = "Email must be valid")
    private String email;

    @JsonProperty("first_name")
    @Size(min = 2, max = 255)
    String firstName;



    String lastName;
    @JsonProperty("password_confirmation")
    String passwordConfirmation;

    String password;


    Set<RoleCreateEditDto> roles;


    @Length(min = 4, max = 255, message = "{errors.user.username.size}")
    @NotNull(message = "{errors.user.username.null}")
    @NotEmpty(message = "{errors.user.username.empty}")
    private String username;

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


    public String getConfirmPassword() {
        return passwordConfirmation;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.passwordConfirmation = confirmPassword;
    }

    public Set<RoleCreateEditDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleCreateEditDto> roles) {
        this.roles = roles;
    }

    /*
        public String getRolesAsString() {
            return roles.stream().collect(Collectors.joining(", "));
        }

        public Set<Role> getRolesSetFromSetString(Set<String> rolesSet) {
            return roles.stream().map(name -> new Role(name.trim())).collect(Collectors.toSet());
        }

        public Set<Role> getRolesSetFromSetString() {
            return getRolesSetFromSetString(getRoles());
        }

        public Set<String> getRoles() {
            return roles;
        }

        public void setRoles(Set<String> roles) {
            this.roles = roles;
        }
    */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
