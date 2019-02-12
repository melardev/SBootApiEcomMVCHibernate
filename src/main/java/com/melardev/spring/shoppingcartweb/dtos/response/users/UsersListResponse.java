package com.melardev.spring.shoppingcartweb.dtos.response.users;

import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.users.partials.UserDataSection;
import com.melardev.spring.shoppingcartweb.models.User;
import org.springframework.data.domain.Page;

public class UsersListResponse extends SuccessResponse {
    private final UserDataSection data;

    public UsersListResponse(UserDataSection data) {
        this.data = data;
    }

    public static UsersListResponse build(Page<User> usersPage, String basePath) {

        return new UsersListResponse(
                UserDataSection.build(usersPage, basePath)
        );
    }

    public UserDataSection getData() {
        return data;
    }
}
