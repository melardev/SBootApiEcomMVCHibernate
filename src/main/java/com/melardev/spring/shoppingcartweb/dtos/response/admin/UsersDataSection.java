package com.melardev.spring.shoppingcartweb.dtos.response.admin;

import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.shared.PageMeta;
import com.melardev.spring.shoppingcartweb.dtos.response.users.UserDto;
import com.melardev.spring.shoppingcartweb.models.User;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersDataSection extends SuccessResponse {
    private Collection<UserDto> users;
    private PageMeta pageMeta;

    public UsersDataSection(PageMeta pageMeta, List<UserDto> userDtos) {
        this.pageMeta = pageMeta;
        this.users = userDtos;
    }

    public static UsersDataSection build(Page<User> users, String usersBasePath) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users.getContent()) {
            userDtos.add(UserDto.build(user));
        }
        return new UsersDataSection(
                PageMeta.build(users, usersBasePath),
                userDtos
        );
    }

    public Collection<UserDto> getUsers() {
        return users;
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public void setPageMeta(PageMeta pageMeta) {
        this.pageMeta = pageMeta;
    }

    public void setUsers(Collection<UserDto> usersDto) {
        this.users = usersDto;
    }

    public void setMeta(PageMeta pageMeta) {
        this.pageMeta = pageMeta;
    }
}
